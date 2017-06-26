package com.android.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//妹夫的，哈哈
public class ImagePath {
    public static final String SERVER_IMAGE = "https://cs.keycoin.cn/";
//    public static String[] IMAGES = {
//            "group1/M00/00/50/wKioZlihGDOAGe99AAJZYBXEasw298.jpg",
//            "group1/M00/00/54/wKioZlk_qlCAdb7wAAQSqI9qDFg344.jpg",
//            "group1/M00/00/54/wKioZlk_qlCAdb7wAAQSqI9qDFg344.jpg",
//            "group1/M00/00/52/wKioZljsicaAcgf8AAFzcPhnPDY296.jpg",
//            "group1/M00/00/52/wKioZljsicCAVFg_AAFzcPhnPDY840.jpg",
//            "group1/M00/00/52/wKioZljsibqAfRBbAAFzcPhnPDY937.jpg",
//            "group1/M00/00/52/wKioZljshsWANmycAADCKLSW92k901.jpg",
//            "group1/M00/00/52/wKioZljshr2AFunBAACr62ZT9D4710.jpg",
//            "group1/M00/00/52/wKioZljsiQWAdzcFAABA29kaR9E917.jpg",
//            "group1/M00/00/52/wKioZljsiP-ANOY9AABA29kaR9E071.jpg",
//            "group1/M00/00/52/wKioZljshnCAK-xfAACyRQ8DbVo961.jpg",
//            "group1/M00/00/52/wKioZljshmeAZShYAACyRQ8DbVo321.jpg",
//            "group1/M00/00/52/wKioZljshl-AKFSXAACyRQ8DbVo336.jpg",
//            "group1/M00/00/52/wKioZljshlqAEKFaAACyRQ8DbVo872.jpg",
//            "group1/M00/00/50/wKioZlihGDOAGe99AAJZYBXEasw298.jpg",
//            "group1/M00/00/54/wKioZlk_qlCAdb7wAAQSqI9qDFg344.jpg",
//            "group1/M00/00/54/wKioZlk_qlCAdb7wAAQSqI9qDFg344.jpg"
//    };


    /** 模拟网络数据 ，写死*/
    public static List<HashMap<String,String>> getListMap(){
        List<HashMap<String,String>> list = new ArrayList<>();

        HashMap<String,String> map1 = new HashMap<>();
        map1.put("sourceType","2");
        map1.put("showType","1");
        map1.put("picUrl","group1/M00/00/50/wKioZlihGDOAGe99AAJZYBXEasw298.jpg");
        map1.put("link","http://www.taobao.com");
        map1.put("title","测试普惠");

        HashMap<String,String> map2 = new HashMap<>();
        map2.put("sourceType","2");
        map2.put("showType","2");
        map2.put("content","<p>阿斯顿发送到<br/></p>");
        map2.put("picUrl","group1/M00/00/54/wKioZlk_qlCAdb7wAAQSqI9qDFg344.jpg");
        map2.put("title","普惠2");

        list.add(map1);
        list.add(map2);
        return list;
    }
}
