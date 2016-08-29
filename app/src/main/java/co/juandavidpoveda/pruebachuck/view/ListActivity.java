package co.juandavidpoveda.pruebachuck.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import co.juandavidpoveda.pruebachuck.AppClass;
import co.juandavidpoveda.pruebachuck.R;
import co.juandavidpoveda.pruebachuck.adapter.DataManager;
import co.juandavidpoveda.pruebachuck.database.DatabaseHelper;
import co.juandavidpoveda.pruebachuck.database.Frase;

public class ListActivity extends ActionBarActivity implements DataManager.DeleteFraseListener {

    private DatabaseHelper dh;
    private AppClass appClass;
    private RecyclerView rv;
    private DataManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        appClass = (AppClass) getApplication();
        dh = appClass.getDbh();

        rv  = (RecyclerView) findViewById(R.id.rv); // layout reference

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        dm = new DataManager(dh.findAllFrases(), ListActivity.this, this);
        rv.setAdapter(dm);
        /*rv.addOnItemTouchListener(
                new RecyclerClickListener(this, new RecyclerClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // STUB:
                        // The click on the item must be handled
                    }
                }));*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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

    @Override
    public void onRemovePFraseClick(Frase frase, int position) {
        System.out.println("*****Delete Item : " + frase.getId());
        dh.deleteFraseById(frase.getId());
        dm.removeDeletedItem(position);
        dm.notifyItemRemoved(position);
        dm.notifyItemRangeChanged(position, dm.tamanoList());
    }
}
