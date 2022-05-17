# GoogleSearch
- Config api.google.key vào file application.properties trong trường hợp muốn tìm kiếm bằng Google Knowledge API
- API chi tiết:
- REQUEST: curl --request POST \
  --url http://localhost:8080/api/search \
  --header 'content-type: application/json' \
  --data '{"system_message":"nguyễn phú trọng"}'
RESPONSE: 
{
    "status_code": 0,
    "status_message": "message success",
    "data": "Nguyễn Phú Trọng Tổng Bí thư thực quyền Ban Chấp hành Trung ương Đảng Cộng sản Việt Nam\nNguyễn Phú Trọng là một chính khách người Việt Nam. Ông hiện đang đảm nhiệm chức vụ Tổng Bí thư Ban Chấp hành Trung ương Đảng Cộng sản Việt Nam; Bí thư Quân ủy Trung ương. Wikipedia\nNgày/nơi sinh:14 tháng 4, 1944 (78 tuổi), Đông Hội\nQuốc tịch:Việt Nam\nVợ/chồng:Ngô Thị Mân\nHọc vấn:Trường Đại học Khoa học Tự nhiên, ĐHQGHN (1963–1967)\nChức vụ:Tổng Bí thư thực quyền Ban Chấp hành Trung ương Đảng Cộng sản Việt Nam từ 2011\nChức vụ trước đó:President of the Vietnam Red Cross Society (2018–2021), THÊM\n"
}
- REQUEST: curl --request POST \
  --url http://localhost:8080/api/knowledge \
  --header 'content-type: application/json' \
  --data '{"system_message":"tào đức thắng"}'
RESPONSE: 
{
    "status_code": 0,
    "status_message": "message success",
    "data": "Tào Đức Thắng\t Tổng giám đốc tập đoàn Viễn Thông quân đội"
}
