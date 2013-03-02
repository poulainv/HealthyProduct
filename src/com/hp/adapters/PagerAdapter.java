package com.hp.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

/**
 * Positionne les Fragments au sein du ViewPager
 * 
 * @author Lisional
 * 
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> fragments;

	/**
	 * @param fm
	 * @param fragments
	 */
	public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;

	}

	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}
}
