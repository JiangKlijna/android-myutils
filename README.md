
#### # JUtil

- android kit,utils...
- android开发的工具
- 推荐使用导入源码的方式的使用(只有java文件)
[点击下载源码](https://raw.githubusercontent.com/JiangKlijna/JUtil/master/library.tar.gz)
#### # 内容说明

包名|工具|描述
----|----|----
com.jiangKlijna.adapter|XAdapter|万能adapter,viewholder以静态内部类的形式存在,通过XAdapter的静态方法获得实例
com.jiangKlijna.application|ActManager|activity的stack式管理
......|AppUtil|应用工具类
......|IntentUtil|Intent工具类
com.jiangKlijna.async|CallBack|继承自Handler的回调
com.jiangKlijna.io|IO|IO工具类
......|FileUtil|android下重要目录获取
com.jiangKlijna.log|L|日志的打印/保存到文件
com.jiangKlijna.math|MathUtil|math工具类
......|Blowfish|加密算法
......|Random|随机数
com.jiangKlijna.net|NetUtil|net工具类
com.jiangKlijna.object|ObjectKey|对象的缓存,可以对一个Object缓存到map,或者对Serializable缓存到文件
......|NullUtil|判断Null工具类
......|ObjUtil|Object工具类,目前有拷贝对象,复制对象
com.jiangKlijna.view|Event|view的事件,事件类以静态内部类的形式存在
......|ImgUtil|Bitmap工具类
......|ToastUtil|toast工具类

#### #示例
 - XAdapter 示例
通过getHolder获得ViewHolder的实例
分别传入一个继承自View的类对象(反射),或者布局资源(inflate)得到convertView
```java
        adapter = new XAdapter<String>(this) {
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
- ObjectKey 示例
    通过方法saveObj_map(保存到map),saveObj_File(保存到文件)得到一个ObjectKey实例,可以通过此实例重新拿到缓存的对象,ObjectKey为Serializable类型,可以通过Intent传递
```java
        ObjectKey<File> key1 = ObjectKey.saveObj_file(Environment.getDataDirectory());
        key1.getObj();//重新获得对象
        key1.popObj();//获得对象后删除缓存
        try {
            key1.prototype();//以缓存中的对象为原型进行拷贝对象
        } catch (Exception e) {
        }
        key1.isDestory();//缓存中的对象是否被销毁
        key1.destory();//销毁缓存中的对象
        key1.updateObj(Environment.getDownloadCacheDirectory());//更新此key所缓存的对象
```


  [1]: https://raw.githubusercontent.com/JiangKlijna/JUtil/master/library.tar.gz