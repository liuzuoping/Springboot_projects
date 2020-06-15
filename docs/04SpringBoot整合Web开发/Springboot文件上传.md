## Springboot文件上传

### Springboot实现单文件上传

在启动类父文件夹下新建文件**FileUploadController.java**

```java
@RestController
public class FileUploadController {
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
    @PostMapping("/upload")
    public String upload(MultipartFile file, HttpServletRequest req){
        String format=sdf.format(new Date());
        String realPath=req.getServletContext().getRealPath("/img")+format;
        File folder=new File(realPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
        String newName= UUID.randomUUID().toString()+oldName.substring(oldName.lastIndexOf("."));
        try {
            file.transferTo(new File(folder,newName));
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/img" + format + newName;
            return url;
        }catch (IOException e){
            e.printStackTrace();
        }
        return "error";
    }
}
```

## 再static包下新建index.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file">
    <input type="submit" value="提交">
</form>
</body>
</html>
```

可以在application.properties中加入上传文件大小限制

```
spring.servlet.multipart.max-file-size=1MB
```

在启动类父文件夹下新建文件MyCustomException.java文件

```java
@ControllerAdvice
public class MyCustomException {
//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public void myexception(MaxUploadSizeExceededException e, HttpServletResponse resp) throws IOException {
//        resp.setContentType("text/html:charset=utf-8");
//        PrintWriter out=resp.getWriter();
//        out.write("上传文件大小超出限制");
//        out.flush();
//        out.close();
//    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView myexception(MaxUploadSizeExceededException e) throws Exception {
        ModelAndView mv=new ModelAndView("myerror");
        mv.addObject("error","上传文件大小超出限制");
        return mv;
    }
}
```

再在templates包下加入错误页面myerror.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<div th:text="${error}"></div>
</body>
</html>
```

### Ajax实现文件上传

在static包下引入jquery.3.3.1.js并创建index2.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="jquery3.3.1.js"></script>
</head>
<body>
<div id="result"></div>
<input type="file" id="file">
<input type="button" value="上传" onclick="uploadFile()">
<script>
    function uploadFile() {
        var file=$("#file")[0].files[0];
        var formData=new FormData();
        formData.append("file",file);
        $.ajax({
            type:'post',
            url:'/upload',
            processData:false,
            contentType:false,
            data:formData,
            success:function (msg) {
                $("#result").html(msg);
            }
        })
    }
</script>
</body>
</html>
```

### Springboot实现多文件上传

FileUploadController中添加如下代码

```java
    @PostMapping("/uploads")
    public String uploads(MultipartFile files[], HttpServletRequest req){
        String format=sdf.format(new Date());
        String realPath=req.getServletContext().getRealPath("/img")+format;
        File folder=new File(realPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        for (MultipartFile file : files) {
            String oldName = file.getOriginalFilename();
            String newName= UUID.randomUUID().toString()+oldName.substring(oldName.lastIndexOf("."));
            try {
                file.transferTo(new File(folder,newName));
                String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/img" + format + newName;
                System.out.println(url);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return "success";
    }
```

在static中添加index3.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="/uploads" method="post" enctype="multipart/form-data">
    <input type="file" name="files" multiple>
    <input type="submit" value="提交">
</form>
</body>
</html>
```

