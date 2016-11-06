package beacon.com.beaconapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import beacon.com.beaconapp.R;

/**
 * Created by abhishek on 11/5/16.
 */
public class BeaconAdapter {

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
    }
}
