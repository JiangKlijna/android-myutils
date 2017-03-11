
#### # [JUtil](https://raw.githubusercontent.com/JiangKlijna/JUtil/master/README.md)

- android kit,utils...
- android开发的工具
- 推荐使用导入源码的方式的使用(只有java文件)
- 用这个[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

#### # 内容说明

包名|工具|描述
----|----|----
com.jiangKlijna|[XAdapter](https://github.com/JiangKlijna/android-myutils/blob/master/app/src/main/java/com/jiangKlijna/XAdapter.java)|万能adapter,viewholder以静态内部类的形式存在,通过XAdapter的静态方法获得实例
......|[IO](https://github.com/JiangKlijna/android-myutils/blob/master/app/src/main/java/com/jiangKlijna/IO.java)|IO工具类
......|[Dir](https://github.com/JiangKlijna/android-myutils/blob/master/app/src/main/java/com/jiangKlijna/Dir.java)|android下重要目录获取
......|[L](https://github.com/JiangKlijna/android-myutils/blob/master/app/src/main/java/com/jiangKlijna/L.java)|日志的打印/保存到文件
......|[Beans](https://github.com/JiangKlijna/android-myutils/blob/master/app/src/main/java/com/jiangKlijna/Beans.java)|对象工厂
......|[Obj](https://github.com/JiangKlijna/android-myutils/blob/master/app/src/main/java/com/jiangKlijna/Obj.java)|Object工具类,目前有拷贝对象,复制对象
......|[Event](https://github.com/JiangKlijna/android-myutils/blob/master/app/src/main/java/com/jiangKlijna/Event.java)|view的事件,事件类以静态内部类的形式存在
......|[Image](https://github.com/JiangKlijna/android-myutils/blob/master/app/src/main/java/com/jiangKlijna/Image.java)|Bitmap工具类

#### #示例
 - XAdapter 示例
> 通过getHolder获得ViewHolder的实例
分别传入一个继承自View的类对象(反射),或者布局资源(inflate)得到convertView
```java
        adapter = new XAdapter<String>(getContext()) {
            @Override
            protected View initData(int position, View convertView, ViewGroup parent) {
                XAdapter.ViewHolder holder = XAdapter.getHolder(getContext(), convertView, TextView.class);
//                XAdapter.ViewHolder holder = XAdapter.getHolder(getContext(), convertView, parent, R.layout.text_item);
                convertView = holder.getConvertView();
                ((TextView) convertView).setText(title[position]);
                return convertView;
            }
        };
```
- Beans 示例
> 通过regist_product方法注册产品
```java
        Beans bs = Beans.getFactory();

        bs.regist_product(Map.class, HashMap.class, Beans.Type.Prototype);
        Map map = bs.getBean(Map.class, HashMap.class);//每一次都会获得不同的map

        bs.regist_product(List.class, ArrayList.class, Beans.Type.Single);
        List list = bs.getBean(List.class, ArrayList.class);//每一次都会获得同一个list

        bs.regist_product(Date.class, Beans.Type.Init, new Date());
        Date date = bs.getBean(Date.class);//每一次都会获得注册时存储的对象

        //可以自定义产品
        bs.regist_product(new Beans.CustomProduct() {
            @Override
            protected Class getIClass() {//产品返回值类型(父类或接口)
                return null;
            }

            @Override
            protected Class getTClass() {//产品本身的类型
                return null;
            }

            @Override
            public Object onGetBean() {//当getBean的时候调用
                return null;
            }

            @Override
            public void onDestory() {//当销毁的时候调用

            }
        });
```
