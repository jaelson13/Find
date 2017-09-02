package find.com.find.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import find.com.find.Fragments.Account_Fragmento;
import find.com.find.Fragments.Maps_Fragmento;
import find.com.find.Fragments.Ranking_Fragmento;

/**
 * Created by Jaelson on 31/08/2017.
 */

public class TabAdapter extends FragmentPagerAdapter {
    int numPaginas;

    public TabAdapter(FragmentManager fm, int numPaginas) {
        super(fm);
        this.numPaginas = numPaginas;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return Ranking_Fragmento.newInstance(position+1);
            case 1:
                return Maps_Fragmento.newInstance(position+1);
            case 2:
                return Account_Fragmento.newInstance(position+1);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numPaginas;
    }
}
