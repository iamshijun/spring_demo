package cjava.walker.testcase.jackson;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cjava.walker.entity.Account;
 
/**
 * <b>function:</b>Jackson ��java����ת����JSON�ַ�����Ҳ���Խ�JSON�ַ���ת����java����
 * jar-lib-version: jackson-all-1.6.2
 * jettison-1.0.1
 * @author hoojo
 * @package com.hoo.test
 * @email hoojo_@126.com
 */
@SuppressWarnings("unchecked")
public class JacksonTest {
    private JsonGenerator jsonGenerator = null;
    private ObjectMapper objectMapper = null;
    private Account bean = null;
    
    @Before
    public void init() {
        bean = new Account();
        bean.setAddress("china-Guangzhou");
        bean.setEmail("hoojo_@126.com");
        bean.setId(1);
        bean.setName("hoojo");
        
        objectMapper = new ObjectMapper();
        try {
            jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @After
    public void destory() {
        try {
            if (jsonGenerator != null) {
                jsonGenerator.flush();
            }
            if (!jsonGenerator.isClosed()) {
                jsonGenerator.close();
            }
            jsonGenerator = null;
            objectMapper = null;
            bean = null;
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
    ����Java����ת����JSON
    1�� JavaBean(Entity/Model)ת����JSON
     * <b>function:</b>��java����ת����json�ַ���
     * @author hoojo
     * @createDate 2010-11-23 ����06:01:10
     */
    @Test
    public void writeEntityJSON() {
        
        try {
            System.out.println("jsonGenerator");
            //writeObject����ת��java����eg:JavaBean/Map/List/Array��
            jsonGenerator.writeObject(bean);    
            System.out.println();
            
            System.out.println("ObjectMapper");
            //writeValue���к�writeObject��ͬ�Ĺ���
            objectMapper.writeValue(System.out, bean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    ���к������£�

    jsonGenerator
    {"address":"china-Guangzhou","name":"hoojo","id":1,"birthday":null,"email":"hoojo_@126.com"}
    ObjectMapper
    {"address":"china-Guangzhou","name":"hoojo","id":1,"birthday":null,"email":"hoojo_@126.com"}
    ����ֱ�����JsonGenerator��writeObject������ObjectMapper��writeValue������ɶ�Java�����ת�������ߴ��ݵĲ���������ķ�ʽ��ͬ��JsonGenerator�Ĵ���������ObjectMapper����Ҳ����˵�����Ҫʹ��JsonGenerator��ת��JSON����ô����봴��һ��ObjectMapper����������ObjectMapper��ת��JSON������ҪJSONGenerator��

    objectMapper��writeValue�������Խ�һ��Java����ת����JSON����������Ĳ���һ����Ҫ�ṩһ���������ת�������ͨ������������ת��������ݡ������ṩһ��File����ת���������д�뵽File�С���Ȼ���������Ҳ���Խ���һ��JSONGenerator��Ȼ��ͨ��JSONGenerator�����ת�������Ϣ���ڶ��������ǽ�Ҫ��ת����Java������������������ķ�������ô��һ��Config�����config�����ṩһЩת��ʱ�Ĺ��򣬹�ָ����Java�����ĳЩ���Խ��й��˻�ת���ȡ�
*/

    /**
    2�� ��Map����ת����Json�ַ���
     * <b>function:</b>��mapת����json�ַ���
     * @author hoojo
     * @createDate 2010-11-23 ����06:05:26
     */
    @Test
    public void writeMapJSON() {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", bean.getName());
            map.put("account", bean);
            bean = new Account();
            bean.setAddress("china-Beijin");
            bean.setEmail("hoojo@qq.com");
            map.put("account2", bean);
            
            System.out.println("jsonGenerator");
            jsonGenerator.writeObject(map);
            System.out.println("");
            
            System.out.println("objectMapper");
            objectMapper.writeValue(System.out, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*    ת���������£�

    jsonGenerator
    {"account2":{"address":"china-Beijin","name":null,"id":0,"birthday":null,"email":"hoojo@qq.com"},"name":"hoojo",
    "account":{"address":"china-Guangzhou","name":"hoojo","id":1,"birthday":null,"email":"hoojo_@126.com"}}
    objectMapper
    {"account2":{"address":"china-Beijin","name":null,"id":0,"birthday":null,"email":"hoojo@qq.com"},"name":"hoojo",
    "account":{"address":"china-Guangzhou","name":"hoojo","id":1,"birthday":null,"email":"hoojo_@126.com"}}
*/

    /**
    3�� ��List����ת����json
     * <b>function:</b>��list����ת����json�ַ���
     * @author hoojo
     * @createDate 2010-11-23 ����06:05:59
     */
    @Test
    public void writeListJSON() {
        try {
            List<Account> list = new ArrayList<Account>();
            list.add(bean);
            
            bean = new Account();
            bean.setId(2);
            bean.setAddress("address2");
            bean.setEmail("email2");
            bean.setName("haha2");
            list.add(bean);
            
            System.out.println("jsonGenerator");
            //listת����JSON�ַ���
            jsonGenerator.writeObject(list);
            System.out.println();
            System.out.println("ObjectMapper");
            //��objectMapperֱ�ӷ���listת���ɵ�JSON�ַ���
            System.out.println("1###" + objectMapper.writeValueAsString(list));
            System.out.print("2###");
            //objectMapper listת����JSON�ַ���
            objectMapper.writeValue(System.out, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    ������£�

    jsonGenerator
    [{"address":"china-Guangzhou","name":"hoojo","id":1,"birthday":null,"email":"hoojo_@126.com"},
    {"address":"address2","name":"haha2","id":2,"birthday":null,"email":"email2"}]
    ObjectMapper
    1###[{"address":"china-Guangzhou","name":"hoojo","id":1,"birthday":null,"email":"hoojo_@126.com"},
    {"address":"address2","name":"haha2","id":2,"birthday":null,"email":"email2"}]
    2###[{"address":"china-Guangzhou","name":"hoojo","id":1,"birthday":null,"email":"hoojo_@126.com"},
    {"address":"address2","name":"haha2","id":2,"birthday":null,"email":"email2"}]
    ������Ƕ��˸�[]�����ţ�ͬ��ArrayҲ����ת����ת����JSON������Ľ����һ���ģ�����Ͳ���ת���ˡ�~.~
*/

    // 4������������jackson�ṩ��һЩ���ͣ�����Щ�������jsonת���������ʹ����Щ����ת��JSON�Ļ�����ô�㼴ʹû��JavaBean(Entity)Ҳ������ɸ��ӵ�Java���͵�JSONת���������õ���Щ���͹���һ�����ӵ�Java���󣬲����JSONת����
    @Test
    public void writeOthersJSON() {
        try {
            String[] arr = { "a", "b", "c" };
            System.out.println("jsonGenerator");
            String str = "hello world jackson!";
            //byte
            jsonGenerator.writeBinary(str.getBytes());
            //boolean
            jsonGenerator.writeBoolean(true);
            //null
            jsonGenerator.writeNull();
            //float
            jsonGenerator.writeNumber(2.2f);
            //char
            jsonGenerator.writeRaw("c");
            //String
            jsonGenerator.writeRaw(str, 5, 10);
            //String
            jsonGenerator.writeRawValue(str, 5, 5);
            //String
            jsonGenerator.writeString(str);
            jsonGenerator.writeTree(JsonNodeFactory.instance.POJONode(str));
            System.out.println();
            
            //Object
            jsonGenerator.writeStartObject();//{
            jsonGenerator.writeObjectFieldStart("user");//user:{
            jsonGenerator.writeStringField("name", "jackson");//name:jackson
            jsonGenerator.writeBooleanField("sex", true);//sex:true
            jsonGenerator.writeNumberField("age", 22);//age:22
            jsonGenerator.writeEndObject();//}
            
            jsonGenerator.writeArrayFieldStart("infos");//infos:[
            jsonGenerator.writeNumber(22);//22
            jsonGenerator.writeString("this is array");//this is array
            jsonGenerator.writeEndArray();//]
            
            jsonGenerator.writeEndObject();//}
            
            
            Account bean = new Account();
            bean.setAddress("address");
            bean.setEmail("email");
            bean.setId(1);
            bean.setName("haha");
            //complex Object
            jsonGenerator.writeStartObject();//{
            jsonGenerator.writeObjectField("user", bean);//user:{bean}
            jsonGenerator.writeObjectField("infos", arr);//infos:[array]
            jsonGenerator.writeEndObject();//}
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*    ���к󣬽�����£�

    jsonGenerator
    "aGVsbG8gd29ybGQgamFja3NvbiE=" true null 2.2c world jac  worl "hello world jackson!" "hello world jackson!"
     {"user":{"name":"jackson","sex":true,"age":22},"infos":[22,"this is array"]} 
    {"user":{"address":"address","name":"haha","id":1,"birthday":null,"email":"email"},"infos":["a","b","c"]}
    ��ô���������json�ַ���������Ľ����һ�µİɡ��ؼ�������JSONGenerator�ṩ�ķ��������һ��Object�Ĺ�����*/


    
    /**
    ����JSONת����Java����
    1�� ��json�ַ���ת����JavaBean����
     * 
     */
    @Test
    public void readJson2Entity() {
        String json = "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}";
        try {
            Account acc = objectMapper.readValue(json, Account.class);
            System.out.println(acc.getName());
            System.out.println(acc);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*�ܼ򵥣��õ���ObjectMapper��������readValue������������������Ҫ�ṩ2����������һ���������ǽ�����JSON�ַ������ڶ��������Ǽ��������JSON������ʲôJava����Java��������͡���Ȼ������������ͬǩ�����������������Ȥ����һһ����ʹ�÷�������Ȼʹ�õķ����͵�ǰʹ�õķ�����ͬС�졣���к󣬽�����£�

    haha
    haha#1#address#null#email*/

    /**
    2�� ��json�ַ���ת����List<Map>����
     * <b>function:</b>json�ַ���ת����list<map>
     * @author hoojo
     * @createDate 2010-11-23 ����06:12:01
     */
    @Test
    public void readJson2List() {
        String json = "[{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"+
                    "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}]";
        try {
            List<LinkedHashMap<String, Object>> list = objectMapper.readValue(json, List.class);
            System.out.println(list.size());
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                Set<String> set = map.keySet();
                for (Iterator<String> it = set.iterator();it.hasNext();) {
                    String key = it.next();
                    System.out.println(key + ":" + map.get(key));
                }
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 /*   ���Թ��������JSONת����List��Ȼ��List�д��AccountBean�������ʧ���ˡ�����֧��Map���ϡ���Ϊ��ת��List.class�����ǲ�֪��List��ź������͡�ֻ��ĬȻMap���͡���Ϊ���еĶ��󶼿���ת����Map��ϣ����к������£�

    2
    address:address2
    name:haha2
    id:2
    email:email2
    address:address
    name:haha
    id:1
    email:email*/

    /**
    3�� Json�ַ���ת����Array���飬��������ķ���ת������ʶ�𵽼����еĶ������͡����������ö������飬���Խ��������⡣ֻ�����������Ǽ��ϣ�����һ�����顣��Ȼ�������Ҫ���������Arrays.asList����ת����List���ɡ�
     * <b>function:</b>json�ַ���ת����Array
     * @author hoojo
     * @createDate 2010-11-23 ����06:14:01
     */
    @Test
    public void readJson2Array() {
        String json = "[{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"+
                "{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}]";
        try {
            Account[] arr = objectMapper.readValue(json, Account[].class);
            System.out.println(arr.length);
            for (int i = 0; i < arr.length; i++) {
                System.out.println(arr[i]);
            }
            
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 /*   ���к�Ľ����

    2
    haha2#2#address2#null#email2
    haha#1#address#null#email*/

    /**
    4�� Json�ַ���ת����Map����
     * <b>function:</b>json�ַ���ת��Map����
     * @author hoojo
     * @createDate Nov 27, 2010 3:00:06 PM
     */
    @Test
    public void readJson2Map() {
        String json = "{\"success\":true,\"A\":{\"address\": \"address2\",\"name\":\"haha2\",\"id\":2,\"email\":\"email2\"},"+
                    "\"B\":{\"address\":\"address\",\"name\":\"haha\",\"id\":1,\"email\":\"email\"}}";
        try {
            Map<String, Map<String, Object>> maps = objectMapper.readValue(json, Map.class);
            System.out.println(maps.size());
            Set<String> key = maps.keySet();
            Iterator<String> iter = key.iterator();
            while (iter.hasNext()) {
                String field = iter.next();
                System.out.println(field + ":" + maps.get(field));
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  /*  ���к������£�

    3
    success:true
    A:{address=address2, name=haha2, id=2, email=email2}
    B:{address=address, name=haha, id=1, email=email}*/


    /**
    �ġ�Jackson��XML��֧��
    JacksonҲ�������java����xml��ת����ת����Ľ��Ҫ��json-lib��ֱ�ۣ�������������stax2-api.jar���jar����
     * <b>function:</b>java����ת����xml�ĵ�
     * ��Ҫ�����jar�� stax2-api.jar
     * @author hoojo
     * @createDate 2010-11-23 ����06:11:21
     */
   /* @Test
    public void writeObject2Xml() {
        //stax2-api-3.0.2.jar
        System.out.println("XmlMapper");
        XmlMapper xml = new XmlMapper();
        
        try {
            //javaBeanת����xml
            //xml.writeValue(System.out, bean);
            StringWriter sw = new StringWriter();
            xml.writeValue(sw, bean);
            System.out.println(sw.toString());
            //Listת����xml
            List<AccountBean> list = new ArrayList<AccountBean>();
            list.add(bean);
            list.add(bean);
            System.out.println(xml.writeValueAsString(list));
            
            //Mapת��xml�ĵ�
            Map<String, AccountBean> map = new HashMap<String, AccountBean>();
            map.put("A", bean);
            map.put("B", bean);
            System.out.println(xml.writeValueAsString(map));
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}


