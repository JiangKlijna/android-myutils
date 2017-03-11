
#### # JUtil

- android kit,utils...
- android开发的工具
- 推荐使用导入源码的方式的使用(只有java文件)
- [点击下载源码](https://raw.githubusercontent.com/JiangKlijna/JUtil/master/library.tar.gz)

#### # 内容说明

包名|工具|描述
----|----|----
com.com.jiangKlijna.adapter|XAdapter|万能adapter,viewholder以静态内部类的形式存在,通过XAdapter的静态方法获得实例
com.com.jiangKlijna.application|ActManager|activity的stack式管理
......|AppUtil|应用工具类
......|IntentUtil|Intent工具类
com.com.jiangKlijna.async|CallBack|继承自Handler的回调
com.com.jiangKlijna.io|IO|IO工具类
......|FileUtil|android下重要目录获取
com.com.jiangKlijna.log|L|日志的打印/保存到文件
......|CrashHandler|android捕获全局异常
com.com.jiangKlijna.math|MathUtil|math工具类
......|Blowfish|加密算法
......|Random|随机数
com.com.jiangKlijna.object|Beans|对象工厂
......|NullUtil|判断Null工具类
......|ObjUtil|Object工具类,目前有拷贝对象,复制对象
com.com.jiangKlijna.view|Event|view的事件,事件类以静态内部类的形式存在
......|ImgUtil|Bitmap工具类
......|ToastUtil|toast工具类

#### #示例
 - XAdapter 示例
> 通过getHolder获得ViewHolder的实例
分别传入一个继承自View的类对象(反射),或者布局资源(inflate)得到convertView
```java
        adapter = new XAdapter<String>(getContext()) {
            @Override
            protected View initData(int position, View convertView, ViewGroup parent) {
                XAdapter.ViewHolder holder = XAdapter.getHolder(getContext(), convertView, TextView.class, position);
//                XAdapter.ViewHolder holder = XAdapter.getHolder(getContext(), convertView, parent, R.layout.text_item, position);
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
