package com.example.usuario.ludiuca;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.usuario.ludiuca.adaptadores.AdaptadorAlumno;
import com.example.usuario.ludiuca.adaptadores.AdaptadorAlumnosGrupo;
import com.example.usuario.ludiuca.clases.SamplePagerItem;
import com.example.usuario.ludiuca.widgets.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fabio on 12/04/2016.
 */
public class AlumnosGrupoActivity extends ActionBarActivity {

    @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragmento_tabs);

            final List<SamplePagerItem> tabs = new ArrayList<SamplePagerItem>();
            tabs.add(new SamplePagerItem("Alumno", Color.WHITE, Color.WHITE));
            ViewPager contenedorTabs = (ViewPager)findViewById(R.id.pager);
            AdaptadorAlumnosGrupo adaptadoralumnosgrupo = new AdaptadorAlumnosGrupo(getSupportFragmentManager(), getApplicationContext());
            contenedorTabs.setAdapter(adaptadoralumnosgrupo);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            final SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidings_tabs);
            mSlidingTabLayout.setViewPager(contenedorTabs);

            mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {

                    return tabs.get(position).getIndicatorColor();
                }

                @Override
                public int getDividerColor(int position) {
                    return tabs.get(position).getDividerColor();
                }
            });

        }
}
