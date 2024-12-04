# 笔记  

### 学号121052022074  姓名朱一杰

## 基础功能

### 时间戳功能实现

#### 实现效果

![](/picture/time.png)

实现步骤

在每个item的布局中加入一行textView 用于显示时间戳内容 id为text_date

![](/picture/timeLayout.png)

布局效果为

![](/picture/timeLayoutR.png)



在projection 中加入 修改时间的投影

![](/picture/timeP1.png)

然后再数据列中加一列NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE

然后在绑定id中加入时间戳text_date 的id

![](/picture/timeP2.png)

 然后查询的数据列下面会多一段数字时间戳然后需要转化时间戳

写一个时间戳处理类

![](/picture/timeP3.png)

然后写一个cursorAdpter进行数据适配在里面获取时间戳并转换为正确的格式

![](/picture/timeP4.png)

从cursor中获取的查询结果给自定义的cursor适配器 最后用这个适配器设置给上下文容器中的listView

然后就可以成功显示时间戳了

![](/picture/timeP5.png)

### 根据内容或者标题查询笔记

### 实现效果

当什么都不输入的时候

![](/picture/seacherR1.png)

输入Note

![](/picture/seacherR.png)

根据内容或者标题进行查询

### 实现步骤

修改菜单头布局list_option_menu.xml文件添加一个搜索项 id为menu_searche ,添加一个没有背景的放大镜图标search6.png

![](/picture/seacherP1.png)

布局实现效果

![](/picture/searchP1R.png)

运行效果的菜单效果

![](/picture/searchP1Run.png)

现在点击按钮是没有任何反应的，因为没有为这个searchview绑定监听事件

##### 为searchview绑定监听事件

从listActivity中获取上下文,然后获取菜单项，把这个菜单项转化为searcherView并且绑定事件重写提交和输入时候的方法

调用一个查询方法handelQuery（query），先定义查询的数据列在dateColunms里面，在定义绑定的视图id

![](/picture/searchP1.png)

##### handleQuery方法查询笔记内容

当query为空的，就默认不调用参数查询，selection=null,selectionArgs=null

当query不为空时候,根据标题或者内容进行模糊查询 当用户提交输入或者输入改变时候就会进行查询

把查询的内容给NotesAdpter再设置给LIstView 就实现了根据笔记内容或者标题查询了！

![](/picture/searchP2F1.png)

![](/picture/searchP2F2.png)

## 附加功能

### ui美化 

### 使用cardView包裹listItem 实现圆角卡片效果和点击附带周围阴影效果

（这里笔记内容和前面的不一样是因为更新了数据库字段，把之前的数据删除了）

#### 实现效果

![](/picture/UIR.png)

![](/picture/UIR1.png)

点击周围会有一圈阴影

#### 实现步骤

##### 1项目添加依赖

```
dependencies {
    implementation 'androidx.cardview:cardview:1.0.0'
}

```

我的项目api版本为35，在项目构建的适合就已经添加了material依赖，这是我的项目依赖,可以看到material的版本为1.12.0

```kotlin
[versions]
agp = "8.6.0"
junit = "4.13.2"
junitVersion = "1.2.1"
espressoCore = "3.6.1"
appcompat = "1.7.0"
material = "1.12.0"
constraintlayout = "2.2.0"
navigationFragment = "2.8.4"
navigationUi = "2.8.4"

[libraries]
junit = { group = "junit", name = "junit", version.ref = "junit" }
ext-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
constraintlayout = { group = "androidx.constraintlayout", name = "constraintlayout", version.ref = "constraintlayout" }
navigation-fragment = { group = "androidx.navigation", name = "navigation-fragment", version.ref = "navigationFragment" }
navigation-ui = { group = "androidx.navigation", name = "navigation-ui", version.ref = "navigationUi" }
[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
```

##### 2. 创建布局文件或者修改布局文件

让listItem的布局被cardView包裹

在cardView中实现圆角和点击阴影

cardvIew_state.xml

```xml
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- 按下时增加阴影 -->
    <item android:state_pressed="true">
        <objectAnimator
            android:propertyName="cardElevation"
            android:duration="200"
            android:valueType="floatType"
            android:valueFrom="4dp"
            android:valueTo="10dp"/>
    </item>
    <!-- 默认状态 -->
    <item>
        <objectAnimator
            android:propertyName="cardElevation"
            android:duration="200"
            android:valueType="floatType"
            android:valueFrom="8dp"
            android:valueTo="4dp"/>
    </item>
</selector>
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardCornerRadius="20dp"
    app:cardElevation="4dp"
    android:layout_margin="8dp"
    android:stateListAnimator="@animator/cardview_state"
    android:background="@drawable/cardview_background_selector"
    android:layout_marginBottom="10dp"
    android:id="@+id/card_view"
    >
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="70dp"

        android:orientation="vertical">

        <!-- 第一行 TextView -->
        <TextView
            android:id="@+id/text_content"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:textAppearance="?android:attr/textAppearanceLarge"
            android:gravity="center_vertical"
            android:paddingTop="10dp"
            android:paddingLeft="20dp"
            android:singleLine="true"
            android:textColor="#333333"
            android:fontFamily="sans-serif-light"
            />

        <!-- 第二行 TextView -->
        <TextView
            android:id="@+id/text_date"
            android:layout_width="match_parent"
            android:layout_height="20dp"
            android:gravity="end"
            android:paddingRight="10dp"
            android:text="2024-11-10-16:33"
            android:textSize="15dp" />

    </LinearLayout>
</com.google.android.material.card.MaterialCardView>
```

##### 3修改主题

使得app的主题为 materrialCompenents下的任意模式的主题，我这里使用的noactionBar，我的菜单头是通过toolbar实现的

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="AppTheme" parent="Theme.MaterialComponents.Light.NoActionBar">
        <!-- 可在此处添加更多样式配置 -->
    </style>
</resources>
```

然后和在manifest里面使用主题

![](/picture/UIP1.png)

##### 4修改notelist的继承把listActivity改成AppCompatActivity这样才能使用cardView

![](/picture/UIP.png)

修改完后会一些报错，添加一个listView和重写一些方法

```java
ListView listView;
setContentView(R.layout.activity_list_with_toolbar);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnCreateContextMenuListener(this);
listView.setOnItemClickListener((parent, view, position, id) -> {
            // 替代 onListItemClick 的逻辑
            onListItemClick(listView, view, position, id);
        });
 protected void onListItemClick(ListView l, View v, int position, long id) {

        // Constructs a new URI from the incoming URI and the row ID
        Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);

        // Gets the action from the incoming Intent
        String action = getIntent().getAction();

        // Handles requests for note data
        if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {

            // Sets the result to return to the component that called this Activity. The
            // result contains the new URI
            setResult(RESULT_OK, new Intent().setData(uri));
        } else {

            // Sends out an Intent to start an Activity that can handle ACTION_EDIT. The
            // Intent's data is the note ID URI. The effect is to call NoteEdit.
            startActivity(new Intent(Intent.ACTION_EDIT, uri));
        }
    }
 public ListAdapter getListAdapter() {
        return listView.getAdapter();
    }
    public long getSelectedItemId() {
        return listView.getSelectedItemId();
    }
```

这样报错就解决能够运行了，但是还是不能显示cardView了，因为更改了视图需要重新匹配

##### 3更新适配器

新建一个cusor适配器

```
public class NotesAdapter extends CursorAdapter {

    public NotesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);  // 传入context、cursor和标志（0是默认的标志，通常用于告诉适配器不需要做额外的工作）
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // 获取视图中的各个组件
        TextView noteTextView = view.findViewById(R.id.text_content);  // 显示笔记内容
        TextView dateTextView = view.findViewById(R.id.text_date);  // 显示日期

        // 获取列索引
        int text = cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_TITLE);
        int time = cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE);
        int tag = cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_COLOR_TAG);

        // 只有列索引有效时才处理数据
        if (text != -1 && time != -1 && tag != -1) {
            // 从cursor中获取数据
            String noteText = cursor.getString(text);
            long timestamp = cursor.getLong(time);  // 假设日期字段是timestamp类型
            String tagColor = cursor.getString(tag);  // 获取颜色字段

            // 设置笔记内容
            noteTextView.setText(noteText);

            // 设置日期，使用自定义的日期格式化方法
            String formattedDate = DateDisplay.showTime(timestamp);
            dateTextView.setText(formattedDate);

            // 设置颜色标签背景
            MaterialCardView cardView = view.findViewById(R.id.card_view); // 假设卡片视图有 ID card_view
            if (tagColor != null) {
                cardView.setCardBackgroundColor(Color.parseColor(tagColor)); // 设置颜色背景
            }
        } else {
            // 如果cursor没有数据，或者列索引无效，可以在这里做相应的错误处理
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // 创建并返回新的视图
        return LayoutInflater.from(context).inflate(R.layout.noteslist_item, parent, false);
    }
}
```

再把listView的适配器改为这个

![](/picture/UIP2.png)

再把其他要用到视图的地方修改一下，数据查询时需要改成，还有noteeditor返回notelist时候需要重新一下restart方法，还有删除笔记的时候

```java
adapter.changeCursor(cursor);  // 更新适配器中的Cursor
 @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }
 // 删除后重新查询数据，并更新适配器
            Cursor newCursor = getContentResolver().query(
                    NotePad.Notes.CONTENT_URI, // 查询笔记数据的 URI
                    null,                      // 获取所有列
                    null,                      // 不需要 where 子句
                    null,                      // 不需要 where 参数
                    null                       // 不需要排序
            );

            // 使用新的 Cursor 更新适配器的数据源
            adapter.changeCursor(newCursor);  // 更新适配器数据

            // 通知适配器刷新视图
            adapter.notifyDataSetChanged();  // 通知适配器视图已更新
```

##### 4运行之后的实现效果

此时还没有颜色，颜色标签修改是另一个附加功能

![](/picture/UIR2.png)

## 附加功能2

### 笔记修改标签颜色和编辑中修改背景颜色

#### 实现效果

![](/picture/UIR.png)

![](/picture/UIP3.png)

点击changeTagColor 然后选择笔记标签颜色为第二个颜色

![](/picture/ColorR2.png)

可以看到点击之后www这个笔记就变成红色了

![](/picture/UIP4.png)

回到主界面已经成功改变笔记颜色了

![](/picture/UIP5.png)

![](/picture/ColorR.png)

![](/picture/UIR3.png)

点击选择背景颜色setBackGround然后在弹出的界面选择浅蓝色



![](/picture/ColorR1.png)

![](/picture/UIR4.png)

![](/picture/UIR5.png)

可以看到背景颜色修改成功了

### 实现步骤

##### 1在数据库中添加2个字段

```java
 public static final String COLUMN_NAME_COLOR_TAG = "color_tag";  // 新增字段：用于存储颜色标签
 public static final String COLUMN_NAME_COLOR_BACKGROUND = "color_background";
```

在notePadProvider里面的stattic url匹配的投影map中添加两个字段

```java
sNotesProjectionMap.put(NotePad.Notes.COLUMN_NAME_COLOR_BACKGROUND,NotePad.Notes.COLUMN_NAME_COLOR_BACKGROUND); sNotesProjectionMap.put(NotePad.Notes.COLUMN_NAME_COLOR_TAG,NotePad.Notes.COLUMN_NAME_COLOR_TAG);
```

更新一下数据库

```java
 @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

           // Logs that the database is being upgraded
           Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                   + newVersion + ", which will destroy all old data");
           if(oldVersion<3){
               db.execSQL("ALTER TABLE notes ADD COLUMN color_backgroud TEXT");
               db.execSQL("ALTER TABLE notes ADD COLUMN color_tag TEXT");
           }

           // Kills the table and existing data
          /* db.execSQL("DROP TABLE IF EXISTS notes");

           // Recreates the database with a new version
           onCreate(db);*/
       }
```

在数据库创建的时候多加两个字段，然后把原来的数据库删除到达更新字段的目的

```java
 @Override
       public void onCreate(SQLiteDatabase db) {
           db.execSQL("CREATE TABLE " + NotePad.Notes.TABLE_NAME + " ("
                   + NotePad.Notes._ID + " INTEGER PRIMARY KEY,"
                   + NotePad.Notes.COLUMN_NAME_TITLE + " TEXT,"
                   + NotePad.Notes.COLUMN_NAME_NOTE + " TEXT,"
                   + NotePad.Notes.COLUMN_NAME_CREATE_DATE + " INTEGER,"
                   + NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE + " INTEGER,"+NotePad.Notes.COLUMN_NAME_COLOR_TAG+" TEXT,"
                   +NotePad.Notes.COLUMN_NAME_COLOR_BACKGROUND+" Text"
                   + ");");
       }
```

2更新菜单布局文件 增加changeTagColor 和setBackGround

这个是上下文菜单

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/context_open"
          android:title="@string/menu_open" />
    <item android:id="@+id/context_copy"
          android:title="@string/menu_copy" />
    <item android:id="@+id/context_delete"
          android:title="@string/menu_delete" />
    <item android:id="@+id/context_change_tag_color"
        android:title="changeTagColor" />
</menu>
```

这个是optionMenu

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/menu_save"
          android:icon="@drawable/ic_menu_save"
          android:alphabeticShortcut='s'
          android:title="@string/menu_save"
          android:showAsAction="ifRoom|withText" />
    <item android:id="@+id/menu_revert"
          android:icon="@drawable/ic_menu_revert"
          android:title="@string/menu_revert" />
    <item android:id="@+id/menu_delete"
          android:icon="@drawable/ic_menu_delete"
          android:title="@string/menu_delete"
          android:showAsAction="ifRoom|withText" />
    <item android:id="@+id/menu_background"
        android:title="setBackGround" />
</menu>
```

##### 3写一个coloradapter用来存储颜色

```java
public  class ColorAdapter extends BaseAdapter {
        private Context context;
        private String[] colors;

        public ColorAdapter(Context context, String[] colors) {
            this.context = context;
            this.colors = colors.clone();
            //Log.d("ColorAdapter", "Colors array: " + Arrays.toString(colors));
        }

        @Override
        public int getCount() {
            return colors.length;
        }

        @Override
        public Object getItem(int position) {
            return colors[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 每个颜色块的布局
            if (convertView == null) {
                convertView = new View(context);
            }
            Log.d("ColorAdapter", "Position: " + position + " Color: " + colors[position]); // 打印每个颜色
            convertView.setLayoutParams(new GridView.LayoutParams(200, 200)); // 设置每个颜色块的大小
            convertView.setBackgroundColor(Color.parseColor(colors[position])); // 设置颜色背景
            final int finalPosition = position;
            // 给颜色块添加点击效果
            /*convertView.setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 更新颜色背景（如果需要动态处理）
                    //v.setBackgroundColor(Color.parseColor(colors[position]));
                    String selectedColor = colors[finalPosition];
                    Log.d("NoteEditor",Arrays.toString(colors));
                    Log.d("NoteEditor", "click"+finalPosition);
                    updateNoteColorBackground(colors[finalPosition]);
                }
            });*/

            return convertView;
        }
    }
```

##### 4.在notelist和noteeditor中完成事件绑定 并写出颜色选择对话框 并将颜色存储到数据库中

```java
if (item.getItemId()==R.id.context_change_tag_color) {
             showColorPickerDialog(position,id);
            return true;
            
```

```java
private void showColorPickerDialog(int pos,long noteId) {
        // 颜色数组，可以加入更多的颜色
        final String[] colors = {
                "#FFFFFF",   // 白色
                "#F4A6B7",   // 柔和的粉色
                "#D3E9F6",   // 浅蓝色
                "#D8E8D5",   // 柔和的绿色
                "#FFDF85",   // 浅黄色
                "#FFD4E1",   // 浅玫瑰红
                "#B2DFF7",   // 天蓝色
                "#C8E6C9",   // 淡绿色
                "#E2B9D3",   // 淡紫色
                "#F5F5F5",   // 浅米色，温和的灰白色
                "#E6E6FA",   // 淡紫色，Lavender
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择笔记标签颜色");

        // 创建一个GridView来显示颜色块
        GridView gridView = new GridView(this);


// 设置GridView的边距，确保GridView可以适应屏幕宽度
        gridView.setPadding(0, 0, 0, 0); // 如果需要，可以根据需要设置额外的内边距

        /*gridView.setLayoutParams(new GridView.LayoutParams(
                GridView.LayoutParams.MATCH_PARENT,
                GridView.LayoutParams.WRAP_CONTENT));  // 确保GridView有足够的高度和宽度*/

// 设置GridView的适配器

        gridView.setNumColumns(6); // 设置每行显示的颜色块数量
        gridView.setHorizontalSpacing(0);  // 设置水平间距
        gridView.setVerticalSpacing(0);    // 设置垂直间距
        gridView.setStretchMode(GridView.STRETCH_SPACING); // 拉伸显示
        int screenWidth = getResources().getDisplayMetrics().widthPixels;// 获取屏幕宽度
        // 设置GridView的宽度和每个项的最大宽度
        GridView.LayoutParams params = new GridView.LayoutParams(screenWidth / 6, GridView.LayoutParams.WRAP_CONTENT);
        gridView.setLayoutParams(params);
        // 创建一个自定义的适配器来显示颜色块
        gridView.setAdapter(new ColorAdapter(this, colors));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
                String selectedColor = colors[position];
                //updateNoteColorBackground(selectedColor); // 更新颜色
                updateNoteColorTag(noteId,selectedColor,pos);//获取笔记的标签和位置
            }
        });

        builder.setView(gridView);
        // 调整对话框的尺寸（增加宽度和高度）
        // 创建对话框
        AlertDialog dialog = builder.create();


        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams dialogParams = window.getAttributes();
            dialogParams.width = (int) (screenWidth * 0.9);  // 设置对话框宽度为屏幕的 90%
            window.setAttributes(dialogParams);
        }
        // 显示对话框
        dialog.show();
        //builder.show();
    }
```

```java
 private void updateNoteColorTag(long noteId, String color,int position) {
        ContentValues values = new ContentValues();
        values.put(NotePad.Notes.COLUMN_NAME_COLOR_TAG, color);

        // 使用ContentResolver更新数据库中的颜色标签
        Uri uri = ContentUris.withAppendedId(NotePad.Notes.CONTENT_URI, noteId);
        getContentResolver().update(uri, values, null, null);  // 更新对应笔记的颜色标签
         changeItemBackgroundColor(position,color);
        // 刷新 UI 或者其他操作
       //refreshNotesList();  // 可以刷新列表或者直接更新显示颜色
    }
```

```java
 private void showColorPickerDialog() {
        // 颜色数组，可以加入更多的颜色
        final String[] colors = {
                "#F0F8FF",   // Alice Blue - 柔和的蓝色，适合长时间阅读
                "#E0FFFF",   // Light Cyan - 淡青色，非常清新
                "#FFF5EE",   // Seashell - 淡贝壳色，温暖柔和
                "#F5FFFA",   // Mint Cream - 薄荷奶油色，清新凉爽
                "#FAF0E6",   // Linen - 亚麻色，带有温暖的感觉
                "#F5F5DC",   // Beige - 米黄色，优雅自然
                "#FFE4B5",   // Moccasin - 驼色，柔和温暖
                "#F4F4F4",   // Soft Gray - 柔和灰色，简洁现代
                "#FFFAF0",   // Floral White - 花卉白，带有轻微的温暖色调
                "#FFF0F5",   // Lavender Blush - 薰衣草红，淡紫色带有微微粉色
                "#F0FFF0",   // Honeydew - 蜂蜜露色，清新柔和
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择背景颜色");

        // 创建一个GridView来显示颜色块
        GridView gridView = new GridView(this);


// 设置GridView的边距，确保GridView可以适应屏幕宽度
        gridView.setPadding(0, 0, 0, 0); // 如果需要，可以根据需要设置额外的内边距

        /*gridView.setLayoutParams(new GridView.LayoutParams(
                GridView.LayoutParams.MATCH_PARENT,
                GridView.LayoutParams.WRAP_CONTENT));  // 确保GridView有足够的高度和宽度*/

// 设置GridView的适配器

        gridView.setNumColumns(6); // 设置每行显示的颜色块数量
        gridView.setHorizontalSpacing(0);  // 设置水平间距
        gridView.setVerticalSpacing(0);    // 设置垂直间距
        gridView.setStretchMode(GridView.STRETCH_SPACING); // 拉伸显示
        int screenWidth = getResources().getDisplayMetrics().widthPixels;// 获取屏幕宽度
      // 设置GridView的宽度和每个项的最大宽度
        GridView.LayoutParams params = new GridView.LayoutParams(screenWidth / 6, GridView.LayoutParams.WRAP_CONTENT);
        gridView.setLayoutParams(params);
        // 创建一个自定义的适配器来显示颜色块
        gridView.setAdapter(new ColorAdapter(this, colors));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
                String selectedColor = colors[position];
                updateNoteColorBackground(selectedColor); // 更新颜色
            }
        });

        builder.setView(gridView);
        // 调整对话框的尺寸（增加宽度和高度）
        // 创建对话框
        AlertDialog dialog = builder.create();


        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams dialogParams = window.getAttributes();
            dialogParams.width = (int) (screenWidth * 0.9);  // 设置对话框宽度为屏幕的 90%
            window.setAttributes(dialogParams);
        }
        // 显示对话框
        dialog.show();
        //builder.show();
```

```java
private void updateNoteColorBackground(String color) {
        if (mUri != null) {

            ContentValues values = new ContentValues();
            values.put(NotePad.Notes.COLUMN_NAME_COLOR_BACKGROUND, color);

            // 更新数据库中的颜色标签字段
            int rowsUpdated = getContentResolver().update(mUri, values, null, null);
            Log.d("NoteEditor", "Rows updated: " + rowsUpdated); // 检查是否更新成功

            // 更新 UI 显示颜色
            mText.setBackgroundColor(Color.parseColor(color));  // 更新背景色
            //mCursor.close();
        } else {
            Log.e("NoteEditor", "mUri is null, unable to update background color.");
        }
    }

```

