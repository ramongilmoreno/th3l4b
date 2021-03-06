package com.th3l4b.srm.android.sqlitetest;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.th3l4b.srm.android.sqlite.AbstractSQLiteOpenHelper;
import com.th3l4b.srm.android.sqlite.ISQLiteModelRuntime;
import com.th3l4b.srm.android.sqlite.SQLiteRuntime;
import com.th3l4b.srm.model.runtime.IFinder;
import com.th3l4b.srm.model.runtime.IModelRuntime;
import com.th3l4b.srm.model.runtime.IUpdater;
import com.th3l4b.srm.sample.base.AbstractModelTest;
import com.th3l4b.srm.sample.base.generated.SampleModelUtils;
import com.th3l4b.srm.sample.base.generated.NoPersistenceSampleRuntime;


public class MainActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AbstractSQLiteOpenHelper asoh = new AbstractSQLiteOpenHelper(this, "SampleBase", null, 1) {
            @Override
            protected IModelRuntime runtime() throws Exception {
                return SampleModelUtils.RUNTIME;
            }
        };
        final SQLiteDatabase database = asoh.getWritableDatabase();
        SQLiteRuntime runtime = new SQLiteRuntime(new NoPersistenceSampleRuntime()) {
            @Override
            protected SQLiteDatabase getDatabase() throws Exception {
                return database;
            }
        };
        final SampleModelUtils smu = new SampleModelUtils(runtime);
        AbstractModelTest amt = new AbstractModelTest() {
            @Override
            protected SampleModelUtils createModelUtils() throws Exception {
                return smu;
            }
        };
        try {
            amt.testAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
}
