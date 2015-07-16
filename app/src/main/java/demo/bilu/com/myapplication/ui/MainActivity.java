package demo.bilu.com.myapplication.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import demo.bilu.com.myapplication.R;
import demo.bilu.com.myapplication.adapter.NormalRecyclerViewAdapter;
import demo.bilu.com.myapplication.model.Code;
import demo.bilu.com.myapplication.net.Api;
import demo.bilu.com.myapplication.net.RequestManager;
import demo.bilu.com.myapplication.ui.view.AutoLoadRecyclerView;


public class MainActivity extends BaseActivity {
    //声明相关变量
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private ArrayAdapter arrayAdapter;
    private AutoLoadRecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private NormalRecyclerViewAdapter mNormalRecyclerViewAdapter;
    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        toolbar.setTitle("全部代码");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.hello_world, R.string.hello_world) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.refresher);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl.setRefreshing(false);
                    }
                }, 3000);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流

        initData();
    }

    private void initData() {
        RequestManager.addRequest(new StringRequest(Api.ALLCODE500,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseHtmlUseJsoup(response);
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }), null);
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
        toolbar.inflateMenu(R.menu.menu_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mRecyclerView = (AutoLoadRecyclerView) findViewById(R.id.recyclerView);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
//        mRecyclerView.s
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void parseHtmlUseJsoup(String html) {
        ArrayList<Code> codes = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        Element divs = doc.getElementById("codelist");
        Elements element = divs.getElementsByClass("codeli");
        Log.d("", "name" + element.size());
        for (int i = 0; i < element.size(); i++) {
            Code code = new Code();
            code.infoUrl = element.get(i).getElementsByClass("codeli-photo").select("a").attr("href").trim();
            code.imageUrl = element.get(i).getElementsByClass("codeli-photo").select("img").attr("src").trim();


            if (element.get(i).getElementsByClass("codeli-info").size() != 0) {
                code.name = element.get(i).getElementsByClass("codeli-info").get(0)
                        .getElementsByClass("codeli-name")
                        .select("a").get(0).text();
                code.info = element.get(i).getElementsByClass("codeli-info").get(0)
                        .getElementsByClass("codeli-description")
                        .get(0).text();
            }
            codes.add(code);
            mNormalRecyclerViewAdapter = new NormalRecyclerViewAdapter(this, codes);
            mRecyclerView.setAdapter(mNormalRecyclerViewAdapter);
        }


    }

}
