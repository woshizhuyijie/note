/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.notepad;

import static android.app.PendingIntent.getActivity;


import com.example.android.notepad.Adapter.NotesAdapter;
import com.google.android.material.card.MaterialCardView;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.app.Fragment;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Displays a list of notes. Will display notes from the {@link Uri}
 * provided in the incoming Intent if there is one, otherwise it defaults to displaying the
 * contents of the {@link NotePadProvider}.
 *
 * NOTE: Notice that the provider operations in this Activity are taking place on the UI thread.
 * This is not a good practice. It is only done here to make the code more readable. A real
 * application should use the {@link android.content.AsyncQueryHandler} or
 * {@link android.os.AsyncTask} object to perform operations asynchronously on a separate thread.
 */
/*
* 把ListActivity->appCompatActivity
* */
public class NotesList extends AppCompatActivity {

    // For logging and debugging
    private static final String TAG = "NotesList";

    /**
     * The columns needed by the cursor adapter
     */
    private static final String[] PROJECTION = new String[] {
            NotePad.Notes._ID, // 0
            NotePad.Notes.COLUMN_NAME_TITLE, // 1
            NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE ,//update_time
            NotePad.Notes.COLUMN_NAME_COLOR_TAG//笔记标签的颜色
    };

    /** The index of the title column */
    private static final int COLUMN_INDEX_TITLE = 1;

    /**
     * onCreate is called when Android starts this Activity from scratch.
     */
    ListView listView;
    NotesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context=this;
        super.onCreate(savedInstanceState);
       /* context.deleteDatabase("note_pad.db");*/
        //设置布局
        setContentView(R.layout.activity_list_with_toolbar);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnCreateContextMenuListener(this);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 替代 onListItemClick 的逻辑
            onListItemClick(listView, view, position, id);
        });
        // The user does not need to hold down the key to use menu shortcuts.
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

        /* If no data is given in the Intent that started this Activity, then this Activity
         * was started when the intent filter matched a MAIN action. We should use the default
         * provider URI.
         */
        // Gets the intent that started this Activity.
        Intent intent = getIntent();

        // If there is no data associated with the Intent, sets the data to the default URI, which
        // accesses a list of notes.
        if (intent.getData() == null) {
            intent.setData(NotePad.Notes.CONTENT_URI);
        }
        // 设置自定义布局
        //setContentView(R.layout.activity_list_with_toolbar);

        // 获取 Toolbar 并设置为支持 ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(TAG);
            setSupportActionBar(toolbar); // 设置为 Activity 的 ActionBar
        }
         // 设置为 Activity 的 ActionBar
        /*
         * Sets the callback for context menu activation for the ListView. The listener is set
         * to be this Activity. The effect is that context menus are enabled for items in the
         * ListView, and the context menu is handled by a method in NotesList.
         */

        /* Performs a managed query. The Activity handles closing and requerying the cursor
         * when needed.
         *
         * Please see the introductory note about performing provider operations on the UI thread.
         */
        Cursor cursor = managedQuery(
            getIntent().getData(),            // Use the default content URI for the provider.
            PROJECTION,                       // Return the note ID and title for each note. and updateDate
            null,                             // No where clause, return all records.
            null,                             // No where clause, therefore no where column values.
            NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
        );

        /*
         * The following two arrays create a "map" between columns in the cursor and view IDs
         * for items in the ListView. Each element in the dataColumns array represents
         * a column name; each element in the viewID array represents the ID of a View.
         * The SimpleCursorAdapter maps them in ascending order to determine where each column
         * value will appear in the ListView.
         */

        // The names of the cursor columns to display in the view, initialized to the title column
        String[] dataColumns = { NotePad.Notes.COLUMN_NAME_TITLE ,NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE} ;

        // The view IDs that will display the cursor columns, initialized to the TextView in
        // noteslist_item.xml
        int[] viewIDs = { android.R.id.text1, R.id.text_date};

        // Creates the backing adapter for the ListView.
        /*SimpleCursorAdapter adapter
            = new SimpleCursorAdapter(
                      this,                             // The Context for the ListView
                      R.layout.noteslist_item,          // Points to the XML for a list item
                      cursor,                           // The cursor to get items from
                      dataColumns,
                      viewIDs
              );*/
      /*  NotesAdapter adapter=new NotesAdapter(
                this,                             // The Context for the ListView
                R.layout.noteslist_item,          // Points to the XML for a list item
                cursor,                           // The cursor to get items from
                dataColumns,
                viewIDs
        );*/
         adapter=new NotesAdapter(this,cursor);
        //CustomCursorAdapter adapter = new CustomCursorAdapter(this, cursor);
        //listView.setAdapter(adapter);

        // Sets the ListView's adapter to be the cursor adapter that was just created.
        //setListAdapter(adapter);
        listView.setAdapter(adapter);
    }

    /**
     * Called when the user clicks the device's Menu button the first time for
     * this Activity. Android passes in a Menu object that is populated with items.
     *
     * Sets up a menu that provides the Insert option plus a list of alternative actions for
     * this Activity. Other applications that want to handle notes can "register" themselves in
     * Android by providing an intent filter that includes the category ALTERNATIVE and the
     * mimeTYpe NotePad.Notes.CONTENT_TYPE. If they do this, the code in onCreateOptionsMenu()
     * will add the Activity that contains the intent filter to its list of options. In effect,
     * the menu will offer the user other applications that can handle notes.
     * @param menu A Menu object, to which menu items should be added.
     * @return True, always. The menu should be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Context context=this;
        NotePadProvider.DatabaseHelper databaseHelper=new NotePadProvider.DatabaseHelper(this);
        databaseHelper.getWritableDatabase();
       /* String[] dataColumns = { NotePad.Notes.COLUMN_NAME_TITLE ,NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE} ;
        int[] viewIDs = { android.R.id.text1, R.id.text_date};*/
        // Inflate menu from XML resource
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_options_menu, menu);
        // 获取搜索图标并设置搜索框监听
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {//执行提交时候的操作
                return handlerQuery(query);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 用户输入时执行的操作

                return handlerQuery(newText);
            }
            /*private boolean handlerQuery(String query){
                if(query==null|| query.equals("")||query.isEmpty()){
                    // 构建查询条件，这里不传入条件，所以会查询所有笔记
                    String selection = null;  // 不做任何筛选
                    String[] selectionArgs = null;

                    // 使用 ContentResolver 查询 NotePad 内容提供者
                    Cursor cursor = getContentResolver().query(
                            NotePad.Notes.CONTENT_URI,  // 笔记表的 URI
                            null,  // 查询所有列
                            selection,  // 不传入筛选条件，查询所有数据
                            selectionArgs,  // 不传入筛选参数
                            NotePad.Notes.DEFAULT_SORT_ORDER  // 默认按修改时间降序排序
                    );

                    *//*NotesAdapter adapter=new NotesAdapter(
                            context,                             // The Context for the ListView
                            R.layout.noteslist_item,          // Points to the XML for a list item
                            cursor,                           // The cursor to get items from
                            dataColumns,
                            viewIDs
                    );*//*
                    NotesAdapter adapter=new NotesAdapter(context,cursor);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    return true;
                }else{

                    // 用户提交查询时执行的操作
                    // 定义查询条件：标题或内容包含
                    String selection = NotePad.Notes.COLUMN_NAME_TITLE + " LIKE ? OR " + NotePad.Notes.COLUMN_NAME_NOTE + " LIKE ?";
                    String[] selectionArgs = new String[]{"%"+query+"%", "%"+query+"%"};

                    // 查询笔记
                    Cursor cursor = getContentResolver().query(
                            NotePad.Notes.CONTENT_URI,  // URI
                            null,  // 查询所有列
                            selection,  // 查询条件
                            selectionArgs,  // 查询条件的参数
                            NotePad.Notes.DEFAULT_SORT_ORDER  // 排序方式（按修改时间倒序）
                    );
                    *//*NotesAdapter adapter=new NotesAdapter(
                            context,                             // The Context for the ListView
                            R.layout.noteslist_item,          // Points to the XML for a list item
                            cursor,                           // The cursor to get items from
                            dataColumns,
                            viewIDs
                    );*//*
                    NotesAdapter adapter=new NotesAdapter(context,cursor);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            }*/
            private boolean handlerQuery(String query) {
                // 如果查询条件为空，查询所有笔记
                if (query == null || query.equals("") || query.isEmpty()) {
                    // 构建查询条件，这里不传入条件，所以会查询所有笔记
                    String selection = null;  // 不做任何筛选
                    String[] selectionArgs = null;

                    // 使用 ContentResolver 查询 NotePad 内容提供者
                    Cursor cursor = getContentResolver().query(
                            NotePad.Notes.CONTENT_URI,  // 笔记表的 URI
                            null,  // 查询所有列
                            selection,  // 不传入筛选条件，查询所有数据
                            selectionArgs,  // 不传入筛选参数
                            NotePad.Notes.DEFAULT_SORT_ORDER  // 默认按修改时间降序排序
                    );

                    // 如果ListView已经设置了适配器，则只需要更新Cursor
                    if (listView.getAdapter() instanceof NotesAdapter) {
                        NotesAdapter adapter = (NotesAdapter) listView.getAdapter();
                        adapter.changeCursor(cursor);  // 更新适配器中的Cursor
                    } else {
                        // 如果ListView没有适配器或适配器不是NotesAdapter，重新设置适配器
                        NotesAdapter adapter = new NotesAdapter(context, cursor);
                        listView.setAdapter(adapter);
                    }

                    return true;
                } else {
                    // 用户提交查询时执行的操作，标题或内容包含查询
                    String selection = NotePad.Notes.COLUMN_NAME_TITLE + " LIKE ? OR " + NotePad.Notes.COLUMN_NAME_NOTE + " LIKE ?";
                    String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%"};

                    // 查询笔记
                    Cursor cursor = getContentResolver().query(
                            NotePad.Notes.CONTENT_URI,  // URI
                            null,  // 查询所有列
                            selection,  // 查询条件
                            selectionArgs,  // 查询条件的参数
                            NotePad.Notes.DEFAULT_SORT_ORDER  // 排序方式（按修改时间倒序）
                    );

                    // 如果ListView已经设置了适配器，则只需要更新Cursor
                    if (listView.getAdapter() instanceof NotesAdapter) {
                        NotesAdapter adapter = (NotesAdapter) listView.getAdapter();
                        Cursor oldCursor = adapter.getCursor();
                        if (oldCursor != null && !oldCursor.isClosed()) {
                            oldCursor.close(); // 关闭旧的Cursor
                        }
                        adapter.changeCursor(cursor);  // 更新适配器中的Cursor
                    } else {
                        // 如果ListView没有适配器或适配器不是NotesAdapter，重新设置适配器
                        NotesAdapter adapter = new NotesAdapter(context, cursor);
                        listView.setAdapter(adapter);
                    }

                    return true;
                }
            }

        });

        // Generate any additional actions that can be performed on the
        // overall list.  In a normal install, there are no additional
        // actions found here, but this allows other applications to extend
        // our menu with their own actions.
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, NotesList.class), null, intent, 0, null);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // The paste menu item is enabled if there is data on the clipboard.
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);


        MenuItem mPasteItem = menu.findItem(R.id.menu_paste);

        // If the clipboard contains an item, enables the Paste option on the menu.
        if (clipboard.hasPrimaryClip()) {
            mPasteItem.setEnabled(true);
        } else {
            // If the clipboard is empty, disables the menu's Paste option.
            mPasteItem.setEnabled(false);
        }

        // Gets the number of notes currently being displayed.
        final boolean haveItems = getListAdapter().getCount() > 0;

        // If there are any notes in the list (which implies that one of
        // them is selected), then we need to generate the actions that
        // can be performed on the current selection.  This will be a combination
        // of our own specific actions along with any extensions that can be
        // found.
        if (haveItems) {

            // This is the selected item.
            Uri uri = ContentUris.withAppendedId(getIntent().getData(), getSelectedItemId());

            // Creates an array of Intents with one element. This will be used to send an Intent
            // based on the selected menu item.
            Intent[] specifics = new Intent[1];

            // Sets the Intent in the array to be an EDIT action on the URI of the selected note.
            specifics[0] = new Intent(Intent.ACTION_EDIT, uri);

            // Creates an array of menu items with one element. This will contain the EDIT option.
            MenuItem[] items = new MenuItem[1];

            // Creates an Intent with no specific action, using the URI of the selected note.
            Intent intent = new Intent(null, uri);

            /* Adds the category ALTERNATIVE to the Intent, with the note ID URI as its
             * data. This prepares the Intent as a place to group alternative options in the
             * menu.
             */
            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);

            /*
             * Add alternatives to the menu
             */
            menu.addIntentOptions(
                Menu.CATEGORY_ALTERNATIVE,  // Add the Intents as options in the alternatives group.
                Menu.NONE,                  // A unique item ID is not required.
                Menu.NONE,                  // The alternatives don't need to be in order.
                null,                       // The caller's name is not excluded from the group.
                specifics,                  // These specific options must appear first.
                intent,                     // These Intent objects map to the options in specifics.
                Menu.NONE,                  // No flags are required.
                items                       // The menu items generated from the specifics-to-
                                            // Intents mapping
            );
                // If the Edit menu item exists, adds shortcuts for it.
                if (items[0] != null) {

                    // Sets the Edit menu item shortcut to numeric "1", letter "e"
                    items[0].setShortcut('1', 'e');
                }
            } else {
                // If the list is empty, removes any existing alternative actions from the menu
                menu.removeGroup(Menu.CATEGORY_ALTERNATIVE);
            }

        // Displays the menu
        return true;
    }

    /**
     * This method is called when the user selects an option from the menu, but no item
     * in the list is selected. If the option was INSERT, then a new Intent is sent out with action
     * ACTION_INSERT. The data from the incoming Intent is put into the new Intent. In effect,
     * this triggers the NoteEditor activity in the NotePad application.
     *
     * If the item was not INSERT, then most likely it was an alternative option from another
     * application. The parent method is called to process the item.
     * @param item The menu item that was selected by the user
     * @return True, if the INSERT menu item was selected; otherwise, the result of calling
     * the parent method.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();
        if (itemId == R.id.menu_add) {
            startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
            return true;
        } else if (itemId == R.id.menu_paste) {
            startActivity(new Intent(Intent.ACTION_PASTE, getIntent().getData()));
            return true;
        } else  {
            return super.onOptionsItemSelected(item);
        }
        /*switch (item.getItemId()) {
        case R.id.menu_add:
          *//*
           * Launches a new Activity using an Intent. The intent filter for the Activity
           * has to have action ACTION_INSERT. No category is set, so DEFAULT is assumed.
           * In effect, this starts the NoteEditor Activity in NotePad.
           *//*
           startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
           return true;
        case R.id.menu_paste:
          *//*
           * Launches a new Activity using an Intent. The intent filter for the Activity
           * has to have action ACTION_PASTE. No category is set, so DEFAULT is assumed.
           * In effect, this starts the NoteEditor Activity in NotePad.
           *//*
          startActivity(new Intent(Intent.ACTION_PASTE, getIntent().getData()));
          return true;
        default:
            return super.onOptionsItemSelected(item);
        }*/
    }

    /**
     * This method is called when the user context-clicks a note in the list. NotesList registers
     * itself as the handler for context menus in its ListView (this is done in onCreate()).
     *
     * The only available options are COPY and DELETE.
     *
     * Context-click is equivalent to long-press.
     *
     * @param menu A ContexMenu object to which items should be added.
     * @param view The View for which the context menu is being constructed.
     * @param menuInfo Data associated with view.
     * @throws ClassCastException
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {

        // The data from the menu item.
        AdapterView.AdapterContextMenuInfo info;

        // Tries to get the position of the item in the ListView that was long-pressed.
        try {
            // Casts the incoming data object into the type for AdapterView objects.
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            // If the menu object can't be cast, logs an error.
            Log.e(TAG, "bad menuInfo", e);
            return;
        }

        /*
         * Gets the data associated with the item at the selected position. getItem() returns
         * whatever the backing adapter of the ListView has associated with the item. In NotesList,
         * the adapter associated all of the data for a note with its list item. As a result,
         * getItem() returns that data as a Cursor.
         */
        Cursor cursor = (Cursor) getListAdapter().getItem(info.position);

        // If the cursor is empty, then for some reason the adapter can't get the data from the
        // provider, so returns null to the caller.
        if (cursor == null) {
            // For some reason the requested item isn't available, do nothing
            return;
        }

        // Inflate menu from XML resource
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);

        // Sets the menu header to be the title of the selected note.
        menu.setHeaderTitle(cursor.getString(COLUMN_INDEX_TITLE));

        // Append to the
        // menu items for any other activities that can do stuff with it
        // as well.  This does a query on the system for any activities that
        // implement the ALTERNATIVE_ACTION for our data, adding a menu item
        // for each one that is found.
        Intent intent = new Intent(null, Uri.withAppendedPath(getIntent().getData(), 
                                        Integer.toString((int) info.id) ));
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, NotesList.class), null, intent, 0, null);
    }

    /**
     * This method is called when the user selects an item from the context menu
     * (see onCreateContextMenu()). The only menu items that are actually handled are DELETE and
     * COPY. Anything else is an alternative option, for which default handling should be done.
     *
     * @param item The selected menu item
     * @return True if the menu item was DELETE, and no default processing is need, otherwise false,
     * which triggers the default handling of the item.
     * @throws ClassCastException
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // The data from the menu item.
        AdapterView.AdapterContextMenuInfo info;
        /*
         * Gets the extra info from the menu item. When an note in the Notes list is long-pressed, a
         * context menu appears. The menu items for the menu automatically get the data
         * associated with the note that was long-pressed. The data comes from the provider that
         * backs the list.
         *
         * The note's data is passed to the context menu creation routine in a ContextMenuInfo
         * object.
         *
         * When one of the context menu items is clicked, the same data is passed, along with the
         * note ID, to onContextItemSelected() via the item parameter.
         */
        try {
            // Casts the data object in the item into the type for AdapterView objects.
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {

            // If the object can't be cast, logs an error
            Log.e(TAG, "bad menuInfo", e);

            // Triggers default processing of the menu item.
            return false;
        }
        // Appends the selected note's ID to the URI sent with the incoming Intent.
        Uri noteUri = ContentUris.withAppendedId(getIntent().getData(), info.id);
        int position = info.position;  // 获取点击项的 position
        Long id=info.id;


        /*
         * Gets the menu item's ID and compares it to known actions.
         */
        if (item.getItemId() == R.id.context_open) {
            // Launch activity to view/edit the currently selected item
            startActivity(new Intent(Intent.ACTION_EDIT, noteUri));

            return true;
        } else if (item.getItemId() == R.id.context_copy) {
            // Gets a handle to the clipboard service.
            ClipboardManager clipboard = (ClipboardManager)
                    getSystemService(Context.CLIPBOARD_SERVICE);

            // Copies the notes URI to the clipboard. In effect, this copies the note itself
            clipboard.setPrimaryClip(ClipData.newUri(   // new clipboard item holding a URI
                    getContentResolver(),               // resolver to retrieve URI info
                    "Note",                             // label for the clip
                    noteUri)                            // the URI
            );
            // Returns to the caller and skips further processing.
            return true;
        } else if (item.getItemId() == R.id.context_delete) {
            // Deletes the note from the provider by passing in a URI in note ID format.
            getContentResolver().delete(
                    noteUri,  // The URI of the provider
                    null,     // No where clause is needed, since only a single note ID is being
                    // passed in.
                    null      // No where clause is used, so no where arguments are needed.
            );
            // Returns to the caller and skips further processing.
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

            return true;
        } else if (item.getItemId()==R.id.context_change_tag_color) {
             showColorPickerDialog(position,id);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
        /*switch (item.getItemId()) {
        case R.id.context_open:
            // Launch activity to view/edit the currently selected item
            startActivity(new Intent(Intent.ACTION_EDIT, noteUri));
            return true;
//BEGIN_INCLUDE(copy)
        case R.id.context_copy:
            // Gets a handle to the clipboard service.
            ClipboardManager clipboard = (ClipboardManager)
                    getSystemService(Context.CLIPBOARD_SERVICE);
  
            // Copies the notes URI to the clipboard. In effect, this copies the note itself
            clipboard.setPrimaryClip(ClipData.newUri(   // new clipboard item holding a URI
                    getContentResolver(),               // resolver to retrieve URI info
                    "Note",                             // label for the clip
                    noteUri)                            // the URI
            );
  
            // Returns to the caller and skips further processing.
            return true;
//END_INCLUDE(copy)
        case R.id.context_delete:
  
            // Deletes the note from the provider by passing in a URI in note ID format.
            // Please see the introductory note about performing provider operations on the
            // UI thread.
            getContentResolver().delete(
                noteUri,  // The URI of the provider
                null,     // No where clause is needed, since only a single note ID is being
                          // passed in.
                null      // No where clause is used, so no where arguments are needed.
            );
  
            // Returns to the caller and skips further processing.
            return true;
        default:
            return super.onContextItemSelected(item);
        }*/
    }

    /**
     * This method is called when the user clicks a note in the displayed list.
     *
     * This method handles incoming actions of either PICK (get data from the provider) or
     * GET_CONTENT (get or create data). If the incoming action is EDIT, this method sends a
     * new Intent to start NoteEditor.
     * @param l The ListView that contains the clicked item
     * @param v The View of the individual item
     * @param position The position of v in the displayed list
     * @param id The row ID of the clicked item
     */

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
    private void changeItemBackgroundColor(int position,String color) {
        // 获取 ListView 中的某个 item 的 View
        View itemView = listView.getChildAt(position);
        if (itemView != null) {
            // 获取该项中的 MaterialCardView
            MaterialCardView cardView = itemView.findViewById(R.id.card_view);

            // 更新背景颜色
            if (cardView != null) {
                cardView.setCardBackgroundColor(Color.parseColor(color));  // 例如设置为红色
            }
        }
    }
    /*@Override
    protected void onResume() {
        super.onResume();
        // 查询所有笔记
        String selection = null;  // 不做任何筛选，查询所有数据
        String[] selectionArgs = null;

        // 使用 ContentResolver 查询 NotePad 内容提供者
        Cursor cursor = getContentResolver().query(
                NotePad.Notes.CONTENT_URI,  // 笔记表的 URI
                null,  // 查询所有列
                selection,  // 不传入筛选条件，查询所有数据
                selectionArgs,  // 不传入筛选参数
                NotePad.Notes.DEFAULT_SORT_ORDER  // 默认按修改时间降序排序
        );

        // 获取当前 ListView 绑定的适配器
        Adapter adapter = listView.getAdapter();
        if (adapter instanceof NotesAdapter) {
            NotesAdapter notesAdapter = (NotesAdapter) adapter;

            // 检查并关闭旧的 Cursor，避免对已关闭的 Cursor 进行操作
            Cursor oldCursor = notesAdapter.getCursor();
            if (oldCursor != null && !oldCursor.isClosed()) {
                oldCursor.close();  // 关闭旧的 Cursor
            }

            // 更新适配器中的 Cursor
            notesAdapter.changeCursor(cursor);  // 重新设置新的 Cursor
        } else {
            // 如果适配器不是 NotesAdapter，创建新的适配器并设置
            NotesAdapter notesAdapter = new NotesAdapter(this, cursor);
            listView.setAdapter(notesAdapter);
        }
    }*/
    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }


}
