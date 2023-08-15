//package com.yl.auth.config;
//
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.http.HttpUtil;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.yl.common.config.BusinessException;
//import com.yl.common.config.Result;
//import com.yl.common.config.ResultCode;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.HashMap;
//import java.util.Map;
//
////@ControllerAdvice
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    // 全局异常处理
//    @ExceptionHandler(value = BusinessException.class)
////    @ResponseBody
//    public Result<Object> handler(BusinessException e) {
//        e.printStackTrace();
//        return Result.error(e.getCode(), e.getMsg());
//    }
//
//    // 全局异常处理
//    @ExceptionHandler(value = NullPointerException.class)
////    @ResponseBody
//    public Result<Object> nullHandler(NullPointerException e) {
//        e.printStackTrace();
//        return Result.error(ResultCode.ERROR.getCode(), e.getMessage());
//    }
//
//
//    public static void main(String[] args) {
//        Map map = new HashMap();
//        map.put("applicationId", "105680");
//        map.put("areaIds", "10296;10297");
//        map.put("url", "https://openapi.gd.10086.cn/eaop/rest/BIGDATA/heatmap/inflowsubscribercounts/v1.1.1");
//
//        String result = HttpUtil.createPost("http://127.0.0.1:28082/visual/honeycomb/api")
//                //String result1 = HttpUtil.createPost("http://10.190.158.99:9090/visual/honeycomb/api")
//                .contentType("application/json")
//                .body(JSONUtil.toJsonStr(map))
//                .execute().body();
//        result = "{\"code\":8000,\"message\":\"服务端异常\",\"data\":\"/ by zero\"}";
//        System.out.println(result);
//        try {
//            JSONObject result1 = JSONUtil.parseObj(result);
//            if (result1.getInt("code") == 200) {
//                JSONObject ydResult = result1.getJSONObject("data");
//                if (ObjectUtil.isNotNull(ydResult) && "0".equals(ydResult.getStr("respcode"))) {
//                    System.out.println(ydResult.getJSONObject("result").toString());
//                    return;
//                } else {
//                    System.out.println("移动系统异常,result:" + JSONUtil.toJsonStr(ydResult));
//                }
//            } else {
//                Map map1 = new HashMap();
//                map1.put("resName", "aaa");
//                map1.put("message", result);
//                HttpUtil.createPost("http://127.0.0.1:28082/dsp/notToken/warnLog/all")
//                        .contentType("application/json")
//                        .body(JSONUtil.toJsonStr(map1))
//                        .execute().body();
//                System.out.println("香蜜湖系统错误,result:" + result1);
//            }
//        } catch (Exception e) {
//            Map map1 = new HashMap();
//            map1.put("resName", "aaa");
//            map1.put("message", result);
//            HttpUtil.createPost("http://127.0.0.1:28082/dsp/notToken/warnLog/all")
//                    .contentType("application/json")
//                    .body(JSONUtil.toJsonStr(map1))
//                    .execute().body();
//            System.out.println("香蜜湖DI系统错误,result:" + result);
//        }
//
//
////        Path sourcePath = Paths.get("E:\\zhy\\old\\ide\\eclipse\\workspace\\cnoocFrame\\cnooc\\com.primeton.zh.manager");
////        List<Map<String, Object>> maps = ExcelUtil.getReader("C:\\Users\\62597\\Desktop\\1.xlsx").readAll();
////        // 开启递归扫描文件
////        String findFileRegex = "*.bizx";
////        List<String> urls = new ArrayList<>();
////        try {
////            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
////                @Override
////                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//////                    System.out.println("准备访问文件，准备访问的文件名是" + dir.getFileName() + " 文件大小为" + attrs.size());
////
////                    try (DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(dir, findFileRegex)) {
////                        newDirectoryStream.forEach(e -> {
////                            // 这里为显示文件的方法
////                            String path = e.toString();
////                            urls.add(path.substring(path.lastIndexOf("\\src\\") + 5).replaceAll("\\\\", ".").replaceAll(".bizx", ".biz.ext"));
////                        });
////                    }
////
////                    return FileVisitResult.CONTINUE;
////                }
////
////                @Override
////                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//////                    System.out.println("正在访问文件，文件名为" + file.getFileName());
////                    return FileVisitResult.CONTINUE;
////                }
////
////                @Override
////                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
////                    return null;
////                }
////
////                @Override
////                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//////                    System.out.println(dir.getFileName() + "已访问结束，准备访问下一个文件");
////                    return FileVisitResult.CONTINUE;
////                }
////            });
////        } catch (IOException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////        Map<String, List<String>> collect = urls.stream().collect(Collectors.groupingBy(e -> e.split("\\.")[4]));
////        collect.forEach((k, v) -> {
////            if (maps.stream().anyMatch(e -> k.equals(e.get("类型").toString()))) {
////                String name = maps.stream().filter(e -> k.equals(e.get("类型").toString())).findFirst().get().get("所属菜单").toString();
////                String parentCode = maps.stream().filter(e -> k.equals(e.get("类型").toString())).findFirst().get().get("编号").toString();
////                v.forEach(e -> {
////                    String[] array = e.split("\\.");
////                    String code = k + "." + array[array.length - 4] + "." + array[array.length - 3];
////                    String url = "/" + array[3] + "/" + array[4] + "/" + e;
////                    System.out.println("INSERT INTO `cnooc_platform_db`.`app_function` (`FUNCCODE`, `FUNCNAME`, `FUNCDESC`, `FUNCACTION`, `PARAINFO`, `ISCHECK`, `FUNCTYPE`, `ISMENU`, `APP_ID`, `TENANT_ID`, `FUNCGROUPID`) " +
////                            "VALUES ('" + code + "', '" + code + "', NULL, '" + url + "', NULL, '1', 'other', '0', NULL, 'default', '" + parentCode + "');\n");
////                });
////            }
////        });
//    }
//
//}
