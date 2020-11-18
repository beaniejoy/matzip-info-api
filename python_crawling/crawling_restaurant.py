import os
import json
import re
import requests
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
from api_key import api_key


# 전체 음식점 리스트 element 추출
def crawl_total_html(target_url_, driver_path_):
    driver = webdriver.Chrome(driver_path_)
    driver.get(target_url_)

    for _ in range(0, 1):
        # while True:
        try:
            more_btn = WebDriverWait(driver, 1).until(
                EC.element_to_be_clickable((By.XPATH, '//span[@class="more-btn"]'))
            )
            driver.execute_script("arguments[0].click();", more_btn)
        except TimeoutException:
            print("All Done")

            _html = driver.page_source
            driver.quit()

            break

    print("All Done")
    _html = driver.page_source
    driver.quit()

    return _html


# kakao api를 이용한 주소 > 위경도 변환
def change_address_kakao_api(address):
    api_url = 'https://dapi.kakao.com/v2/local/search/address.json?query=' + address
    headers = {'Authorization': 'KakaoAK ' + api_key}
    result_ = json.loads(str(requests.get(api_url, headers=headers).text))

    # kakao api 호출시 빈 리스트 반환하는 경우
    if result_['documents']:
        x = result_['documents'][0]['x']
        y = result_['documents'][0]['y']
    else:
        x = -1
        y = -1

    return x, y


# 개별 음식점 element 파싱
def crawl_detail_html(detail_url_):
    detail_html = requests.get(detail_url_)
    detail_soup = BeautifulSoup(detail_html.text, "html.parser")

    # 이미지 추출
    res_img_tag = detail_soup.find(attrs={'class': 'store-pic'}).find('li', attrs={'class': 'bimg'}).find('img')
    res_img_url = res_img_tag['src']

    res_name = detail_soup.find(attrs={'class': 'tit-point'}).text
    res_name = res_name.strip('\n')
    print('detail crawling... about ' + res_name)
    res_address = detail_soup.find(attrs={'class': 'locat'}).text
    res_phone = detail_soup.find(attrs={'class': 'tel'}).text

    # 설명태그 추출
    res_description = ''
    res_description_list = detail_soup.find(attrs={'class': 'pic-grade'}).find(attrs={'class': 'btxt'}).find_all('a')
    for desc_element in res_description_list:
        desc = desc_element.text.strip()
        if res_description == '':
            res_description += desc
        else:
            res_description += (', ' + desc)

    # 태그정보(list)
    tag_list = []
    res_tag_element = detail_soup.find(attrs={'class': 'tag'})
    if res_tag_element is not None:
        res_tag_list = res_tag_element.find_all('span')

        for tag_element in res_tag_list:
            tag_name = tag_element.text.strip()

            tag = {'name': tag_name}

            tag_list.append(tag)

    # 영업시간(list)
    oper_time_list = []
    res_oper_time_element = detail_soup.find(attrs={'class': 'busi-hours'})
    if res_oper_time_element is not None:
        res_oper_time_list = res_oper_time_element.find_all('li')

        for oper_time_element in res_oper_time_list:
            day = oper_time_element.find(attrs={'class': 'l-txt'}).text.strip()

            # p tag 내부 span태그 제거를 위한 작업
            p_tag = oper_time_element.find(attrs={'class': 'r-txt'})
            span_tag = oper_time_element.find(attrs={'class': 'r-txt'}).find('span')

            if span_tag is not None:
                span_tag.decompose()

            time = p_tag.text.strip()

            oper_time = {'day': day,
                         'time': time}

            oper_time_list.append(oper_time)

    # 메뉴정보(list)
    menu_list = []
    # 메뉴가 존재하지 않는 경우 존재
    res_menu_element = detail_soup.find(attrs={'class': 'Restaurant_MenuList'})
    if res_menu_element is not None:
        res_menu_list = res_menu_element.find_all('li')

        for menu_element in res_menu_list:
            menu_name = menu_element.find(attrs={'class': 'Restaurant_Menu'}).text.strip()
            menu_price = menu_element.find(attrs={'class': 'Restaurant_MenuPrice'}).text.strip()

            menu = {'name': menu_name,
                    'price': menu_price}

            menu_list.append(menu)

    # 음식점 등급(종합, 맛, 가격, 서비스, 평가인원수)
    res_grade_element = detail_soup.find(attrs={'class': 'appraisal'})
    if res_grade_element is not None:
        number_str = res_grade_element.find(attrs={'class': 'tit'}).text.strip()
        number = int(re.findall('\d+', number_str)[0])
        total = float(res_grade_element.find(attrs={'class': 'point'}).text.strip()[:-1])

        point_detail_list = res_grade_element.find(attrs={'class': 'point-detail'})
        point_taste = float(point_detail_list.find_all('span')[0].find(attrs={'class': 'star'}).text.strip())
        point_price = float(point_detail_list.find_all('span')[1].find(attrs={'class': 'star'}).text.strip())
        point_service = float(point_detail_list.find_all('span')[2].find(attrs={'class': 'star'}).text.strip())

        grade = {'total': total,
                 'taste': point_taste,
                 'price': point_price,
                 'service': point_service,
                 'number': number}

    # 위도, 경도로 주소 변환 (kakao api 사용)
    x, y = change_address_kakao_api(res_address)

    # 단일 음식점에 대한 json 데이터 생성
    _single_data = {'name': res_name,
                    'imgUrl': res_img_url,
                    'phoneNumber': res_phone,
                    'address': res_address,
                    'operTime': oper_time_list,
                    'lat': x,
                    'lng': y,
                    'menuItems': menu_list,
                    'description': res_description,
                    'tag': tag_list,
                    'grade': grade}

    return _single_data


def crawling():
    # URL setting
    domain_url = "https://www.diningcode.com"
    target_url = "/list.php?query=%EA%B3%B5%EB%8D%95%EC%97%AD"

    # webdriver 실행파일 있는 파일위치
    driver_path = "C:\dev\python_crawling\chromedriver.exe"
    # driver_path = "/home/ec2-user/app/matzip/chromedriver.exe"

    # 해당구역 내 전체 음식점 리스트 정보 추출
    html = crawl_total_html(domain_url + target_url, driver_path)
    soup = BeautifulSoup(html, "html.parser")

    # 100개 수집 확인
    print('total:', len(soup.find_all(attrs={"onmouseenter": True})))

    # 전체 음식점 리스트
    res_data = []

    # 링크를 통한 음식점 상세페이지 내용 조회
    for element in soup.find_all(attrs={"onmouseenter": True}):
        detail_url = domain_url + element.find('a').get('href')

        # 개별 음식점 링크 주소를 통한 파싱
        single_data = crawl_detail_html(detail_url)

        res_data.append(single_data)

    return res_data


# json 파일로 생성
def make_json(src, result_):
    if not os.path.isdir(src):
        os.mkdir(src)

    with open(src + '/matzip.json', 'w', encoding='utf-8-sig') as make_file:
        json.dump(result_, make_file, indent='\t', ensure_ascii=False)

    print('Success: making json file!!')


result = crawling()

# cloud linux 환경에서 src 경로 확인
src_dir = './json'
make_json(src_dir, result)
