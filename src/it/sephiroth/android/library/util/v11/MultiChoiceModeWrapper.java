package it.sephiroth.android.library.util.v11;

import it.sephiroth.android.library.widget.AbsHListView;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

public class MultiChoiceModeWrapper implements MultiChoiceModeListener {

	private MultiChoiceModeListener mWrapped;
	private AbsHListView mView;
	
	public MultiChoiceModeWrapper( AbsHListView view ) {
		mView = view;
	}

	public void setWrapped( MultiChoiceModeListener wrapped ) {
		mWrapped = wrapped;
	}

	public boolean hasWrappedCallback() {
		return mWrapped != null;
	}

	@TargetApi( Build.VERSION_CODES.HONEYCOMB )
	/*public boolean onCreateActionMode( ActionMode mode, Menu menu ) {
		if ( mWrapped.onCreateActionMode( mode, menu ) ) {
			mView.setLongClickable( false );
			return true;
		}
		return false;
	}

	@TargetApi( Build.VERSION_CODES.HONEYCOMB )
	public boolean onPrepareActionMode( ActionMode mode, Menu menu ) {
		return mWrapped.onPrepareActionMode( mode, menu );
	}

	@TargetApi( Build.VERSION_CODES.HONEYCOMB )
	public boolean onActionItemClicked( ActionMode mode, MenuItem item ) {
		return mWrapped.onActionItemClicked( mode, item );
	}

	@TargetApi( Build.VERSION_CODES.HONEYCOMB )
	public void onDestroyActionMode( ActionMode mode ) {
		mWrapped.onDestroyActionMode( mode );
		mView.mChoiceActionMode = null;

		// Ending selection mode means deselecting everything.
		mView.clearChoices();
		mView.mDataChanged = true;
		mView.rememberSyncState();
		mView.requestLayout();
		mView.setLongClickable( true );
	}*/

//	@TargetApi( Build.VERSION_CODES.HONEYCOMB )
	/*public void onItemCheckedStateChanged( ActionMode mode, int position, long id, boolean checked ) {
		mWrapped.onItemCheckedStateChanged( mode, position, id, checked );
		
		// If there are no items selected we no longer need the selection mode.
		if ( mView.getCheckedItemCount() == 0 ) {
			mode.finish();
		}
	}*/

	@Override
	public boolean onActionItemClicked(android.support.v7.view.ActionMode arg0,
			MenuItem arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCreateActionMode(android.support.v7.view.ActionMode arg0,
			Menu arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDestroyActionMode(android.support.v7.view.ActionMode arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onPrepareActionMode(android.support.v7.view.ActionMode arg0,
			Menu arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemCheckedStateChanged(
			android.support.v7.view.ActionMode mode, int position, long id,
			boolean checked) {
		// TODO Auto-generated method stub
		
	}
}