package beacon.com.test.adapter;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import beacon.com.test.R;
import beacon.com.test.model.BeaconItem;

/**
 * Created by abhishek on 11/5/16.
 */
public class BeaconAdapter extends RecyclerView.Adapter<BeaconAdapter.BeaconItemViewHolder> {

    private static final String TAG = BeaconAdapter.class.getSimpleName();

    private SortedList<BeaconItem> beaconItemList;
    private Context context;

    public BeaconAdapter(final Context context) {
        this.context = context;
        this.beaconItemList = new SortedList<>(BeaconItem.class, new SortedList.Callback<BeaconItem>() {
            @Override
            public int compare(BeaconItem o1, BeaconItem o2) {
                if(o1.getRssi() < o2.getRssi()) {
                    return 1;
                } else if(o1.getRssi() > o2.getRssi()) {
                    return -1;
                }
                return 0;
            }

            @Override
            public void onInserted(int position, int count) {
                Log.v(TAG, "onInserted called " + position + count);
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(BeaconItem oldItem, BeaconItem newItem) {
                // return whether the items' visual representations are the same or not.
                return oldItem.getTitle().equals(newItem.getTitle()) &&
                        oldItem.getBeaconId().equals(newItem.getBeaconId());
            }

            @Override
            public boolean areItemsTheSame(BeaconItem item1, BeaconItem item2) {
                return item1.getBeaconId().equals(item2.getBeaconId())
                        && item1.getTitle().equals(item2.getTitle());
            }
        });
    }


    @Override
    public BeaconItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.beacon_card, null);
        BeaconItemViewHolder beaconItemViewHolder = new BeaconItemViewHolder(view);
        return beaconItemViewHolder;
    }

    @Override
    public void onBindViewHolder(BeaconItemViewHolder holder, int position) {
        BeaconItem item = beaconItemList.get(position);
        holder.getBeaconId().setText(item.getBeaconId());
        holder.getTitle().setText(item.getTitle());
        holder.getDescription().setText(item.getDescription());
        holder.getUrl().setText(item.getUrl());
    }

    public void add(BeaconItem item) {
        beaconItemList.add(item);
        Log.v(TAG, "Beacon Item inserted " + item.getBeaconId());
        Log.v(TAG, "List size is now " + beaconItemList.size());
    }



    @Override
    public int getItemCount() {
        return (null != beaconItemList ? beaconItemList.size() : 0);
    }

    class BeaconItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView beaconId;
        protected TextView title;
        protected TextView description;
        protected TextView url;

        public BeaconItemViewHolder(View view) {
            super(view);
            this.beaconId = (TextView) view.findViewById(R.id.beaconId);
            this.title = (TextView) view.findViewById(R.id.infoTitle);
            this.description = (TextView) view.findViewById(R.id.description);
            this.url = (TextView) view.findViewById(R.id.url);
        }

        public TextView getBeaconId() {
            return beaconId;
        }

        public void setBeaconId(TextView beaconId) {
            this.beaconId = beaconId;
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }

        public TextView getDescription() {
            return description;
        }

        public void setDescription(TextView description) {
            this.description = description;
        }

        public TextView getUrl() {
            return url;
        }

        public void setUrl(TextView url) {
            this.url = url;
        }
    }
}
