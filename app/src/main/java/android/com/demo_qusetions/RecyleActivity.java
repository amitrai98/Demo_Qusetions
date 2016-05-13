package android.com.demo_qusetions;

import android.com.demo_qusetions.adapter.MyAdapter;
import android.com.demo_qusetions.bin.UserBin;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyleActivity extends AppCompatActivity {

    private RecyclerView recyclerView = null;
    private List<UserBin> listUser = new ArrayList<>();
    private MyAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyle);
        initView();
    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        listUser.clear();
        adapter = new MyAdapter(listUser);
        recyclerView.setAdapter(adapter);

        listUser.add(new UserBin("amit","evon"));
        listUser.add(new UserBin("amit","evon"));
        listUser.add(new UserBin("amit","evon"));
        listUser.add(new UserBin("amit","evon"));
        listUser.add(new UserBin("amit","evon"));
        listUser.add(new UserBin("amit","evon"));
        listUser.add(new UserBin("amit","evon"));
        listUser.add(new UserBin("amit","evon"));
        listUser.add(new UserBin("amit","evon"));

        adapter.notifyDataSetChanged();
    }

    /**
     * sends bundle from on class to another
     */
    private void sendBundle(){
//        UserBin ubin = new UserBin("name", "address");
//        Intent i = new Intent(this, MainActivity.class);
//        i.putExtra("userdata", ubin);
//        startActivity(i);
    }
}
