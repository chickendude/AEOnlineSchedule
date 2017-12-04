package ch.ralena.aeonlineschedule.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class AEAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	static int TYPE_NORMAL = 1;
	static int TYPE_BOTTOM = 2;

	List<T> objectsList;

	public AEAdapter(List<T> objectsList) {
		this.objectsList = objectsList;
	}

	@Override
	public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (position < objectsList.size()) {
			((AEViewHolder) holder).bindView(objectsList.get(position));
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (position < objectsList.size()) {
			return TYPE_NORMAL;
		} else {
			return TYPE_BOTTOM;
		}
	}

	@Override
	public int getItemCount() {
		return objectsList.size();
	}

	abstract class AEViewHolder extends RecyclerView.ViewHolder {

		AEViewHolder(View itemView) {
			super(itemView);
			loadViews();
		}

		abstract void loadViews();

		abstract void bindView(T object);
	}

	abstract class AEViewHolderBottom extends RecyclerView.ViewHolder {

		AEViewHolderBottom(View itemView) {
			super(itemView);
			loadViews();
		}

		abstract void loadViews();
	}
}
