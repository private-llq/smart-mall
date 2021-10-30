package com.jsy.utils;

public abstract class FilePathGenerator {
    public abstract String get(FileType fileType);


    public static enum FileType {
        IMAGE("img"),
        WORD("templates"),
        EXCEL("excel"),
        PPT("ppt"),
        TXT("txt"),
        PDF("pdf"),
        ZIP("zip"),
        RAR("rar"),
        FILE("file");

        private String ftype;

        FileType(String type) {
            ftype = type;
        }


        public String getFtype() {
            return ftype;
        }


        /**
         * 根据文件后缀,获取内置的文件类型
         * @param extension
         * @return
         */
        public static FileType getFileTypeByExtension(String extension) {
            if(null == extension){
                return FILE;
            } else if(extension.startsWith(".")){
                extension = extension.substring(1);
            }
            if ("png,jpg,jpeg,bmp,gif,tiff,raw".contains(extension.toLowerCase())) {
                return IMAGE;
            } else if("doc, docx, dot".contains(extension.toLowerCase())){
                return WORD;
            }else if("ppt, pptx".contains(extension.toLowerCase())){
                return PPT;
            }else if("xls, xlsx".contains(extension.toLowerCase())){
                return EXCEL;
            } else if("txt".contains(extension.toLowerCase())){
                return TXT;
            } else if("pdf".contains(extension.toLowerCase())){
                return PDF;
            }else if("zip".contains(extension.toLowerCase())){
                return ZIP;
            }else if("rar".contains(extension.toLowerCase())){
                return RAR;
            }
            return FILE;

        }
    }
}
