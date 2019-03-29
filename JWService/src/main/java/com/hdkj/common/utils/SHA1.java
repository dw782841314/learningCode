package com.hdkj.common.utils;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {
	private static Logger log1 = Logger.getLogger("log1");//查询缴费清单日志模块
	 /** 
     * 微信支付签名算法sign
     * @return 
     */  
    public static String SHA1(String str) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA1"); //如果是SHA加密只需要将"SHA-1"改成"SHA"即可
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexStr = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    
   
 /*   *//**
     *  
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br> 
     * 实现步骤: <br> 
     *  
     * @param paraMap   要排序的Map对象 
     * @param urlEncode   是否需要URLENCODE 
     * @param keyToLower    是否需要将Key转换为全小写 
     *            true:key转化成小写，false:不转化 
     * @return 
     *//*
    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower)  
    {  
        String buff = "";  
        Map<String, String> tmpMap = paraMap;  
        try  
        {  
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());  
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）  
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()  
            {  
   
                @Override  
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)  
                {  
                    return (o1.getKey()).toString().compareTo(o2.getKey());  
                }  
            });  
            // 构造URL 键值对的格式  
            StringBuilder buf = new StringBuilder();  
            for (Map.Entry<String, String> item : infoIds)  
            {  
                if (StringUtils.isNotBlank(item.getKey()))  
                {  
                    String key = item.getKey();  
                    String val = item.getValue();  
                    if (urlEncode)  
                    {  
                        val = URLEncoder.encode(val, "utf-8");  
                    }  
                    if (keyToLower)  
                    {  
                        buf.append(key.toLowerCase() + "=" + val);  
                    } else  
                    {  
                        buf.append(key + "=" + val);  
                    }  
                    buf.append("&");  
                }  
   
            }  
            buff = buf.toString();  
            if (buff.isEmpty() == false)  
            {  
                buff = buff.substring(0, buff.length() - 1);  
            }  
        } catch (Exception e)  
        {  
           return null;  
        }  
        return buff;  
    }  
    
    public static boolean getAuthenticationbyKey(String xml,String key){
    	boolean flag=false;
    	try{
    		String WinxinSgin=XmlUtil.getMap(xml).get("sign");//得到微信签名
    		if(WinxinSgin==null){
    			return false;
    		}
        	String MySign=getMySignByKey(xml,key);//得到我的签名
        	if(WinxinSgin.equals(MySign)){
        		flag= true;
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	
    	return flag;
    }

    *//**
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3 微信签名官方文档地址
     * @param sgin 微信签名字符串
     * @param MySgin 我的签名字符串
     * @return true通过 false 失败
     *//*
    public static boolean getAuthentication(String xml){
    	boolean flag=false;
    	*//*String WinxinSgin=XmlUtil.getMap(xml).get("sign");//得到微信签名
    	String strXML=XmlUtil.getMap(xml).get("strXML");//去除签名之后的xml
    	Map<String, String> paraMap=XmlUtil.xmlToMap(strXML);//将xml最底层节点里面的参数转换成map键值对，
    	String SortUrl=formatUrlMap(paraMap,false , false);//对url拼接成key value形式按照ASCII从小到大排序
    	String key=FtpUtil.getValue("key");//注：key为商户平台设置的密钥key
    	String MySign=SHA1(SortUrl);//对url生成我的签名
    	if(WinxinSgin.equals(MySign)){
    		flag= true;
    	}*//*
    	try{
    		String WinxinSgin=XmlUtil.getMap(xml).get("sign");//得到微信签名
    		//log1.info("==获取的微信签名为："+WinxinSgin);
        	String MySign=getMySign(xml);//得到我的签名
        	//log1.info("==得到我的签名为："+MySign);
        	if(WinxinSgin.equals(MySign)){
        		flag= true;
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	
    	return flag;
    }*/
    /**
     * Str1 = Sha1（xml 数据包+密钥），密钥拼接在 xml 数据包后面参与签名
	       签名串= toupper(Str1)，转换为大写字符
     * @param xml 通信数据包
     * @return 
     */
    public static String getMySign(String xml){
    	return  SHA1(xml+"192006250b4c09247ec02edce69f6a2d").toUpperCase();//xml+秘钥生成签名转换为大写。
    }
    
    public static String getMySignByKey(String xml, String key){
    	return  SHA1(xml+key).toUpperCase();//xml+秘钥生成签名转换为大写。
    }
    
//    public static void main(String[] args) {
//    	String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><wxlifepay><head><version>1.0.1</version><trancode>query</trancode><transeqnum>2123353325</transeqnum><merchantid>1487991442</merchantid></head><info><bill_key>2017199698</bill_key><company_id>42010000310201</company_id><begin_num>1</begin_num><query_num>1</query_num></info></wxlifepay>";
//    	System.out.println(getMySign(xml));
//	}
}
