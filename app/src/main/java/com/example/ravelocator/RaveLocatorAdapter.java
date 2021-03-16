package com.example.ravelocator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ravelocator.util.Datum;
import com.example.ravelocator.util.DatumFavoriteUpdate;
import com.example.ravelocator.util.Venue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import xyz.hanks.library.bang.SmallBangView;

public class RaveLocatorAdapter extends RecyclerView.Adapter<RaveLocatorAdapter.RaveViewHolder>{

    private LayoutInflater mInflater;
    private List<Datum> datum;
    private Venue venue;
    private List<Datum> favorites;
    private DatumFavoriteUpdate isFavorite;
    private final static int MAX_LINES_COLLAPSED = 1;
    private final boolean INITIAL_IS_COLLAPSED = true;
    private boolean isCollapsed = INITIAL_IS_COLLAPSED;
    private ListItemClickListener mOnClickListener;
    private Toolbar toolbar;
    private RaveLocatorViewModel mRaveLocatorViewModel;
    private List<DatumWithVenue> dwv;
    private List<VenueWithDatum> vwd;
    private String location;
    private boolean isLocationQuery = false;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    Date todayDate = Calendar.getInstance().getTime();
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String today = formatter.format(todayDate);
    class RaveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView concertName;
        private final TextView concertVenue;
        private final TextView artistPreview;
        private final TextView separatorView;
        private final SmallBangView imageView;
        private final TextView initialSeparatorView;
        private final LinearLayout container;
        private final ImageButton itemOptions;
        private final TextView location;
        private final TextView justAdded;
        private RaveViewHolder(View itemView){
            super(itemView);
            calendar = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            date = dateFormat.format(calendar.getTime());
            imageView = itemView.findViewById(R.id.imageViewAnimation);
            container = itemView.findViewById(R.id.recyclerview);
            concertName = itemView.findViewById(R.id.concertName);
            concertVenue = itemView.findViewById(R.id.concertVenue);
            artistPreview = itemView.findViewById(R.id.artistPreview);
            separatorView = itemView.findViewById(R.id.separatorView);
            initialSeparatorView = itemView.findViewById(R.id.initialSeparator);
            itemOptions = itemView.findViewById(R.id.itemOptions);
            location = itemView.findViewById(R.id.location);
            justAdded = itemView.findViewById(R.id.justAdded);
            itemView.setOnClickListener(this);
            itemView.findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageView.isSelected()) {
                        imageView.setSelected(false);
                    } else {
                        // if not selected only
                        // then show animation.
                        imageView.setSelected(true);
                        imageView.likeAnimation();
                    }
                    int position = getBindingAdapterPosition();
                    mOnClickListener.onListItemClick(datum.get(position));
                }
            });
            itemView.findViewById(R.id.itemOptions).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(v);
                }
            });
        }

        private void showPopupMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.event_page:
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(datum.get(getAbsoluteAdapterPosition()).getLink()));
                            view.getContext().startActivity(browserIntent);
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popupMenu.show();
        }

        @Override
        public void onClick(View v) {
//            int position = getBindingAdapterPosition();
//            mOnClickListener.onListItemClick(position);
        }
    }
    interface ListItemClickListener{
        void onListItemClick(Datum datum);
    }

    public RaveLocatorAdapter(Context context, ListItemClickListener onClickListener) {
        mInflater = LayoutInflater.from(context);
        this.mOnClickListener = onClickListener;
        setHasStableIds(true);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @NonNull
    @Override
    public RaveLocatorAdapter.RaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View concertName = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        return new RaveViewHolder(concertName);
    }

    @Override
    public void onBindViewHolder(@NonNull RaveLocatorAdapter.RaveViewHolder holder, int position) {
        holder.container.setAnimation(AnimationUtils.loadAnimation(holder.container.getContext(), R.anim.fade_scale_animation));
        Datum current = datum.get(position);

        if(current.getFavorite() == true){
            holder.imageView.setSelected(true);
        }
        String createdDate = current.getCreatedDate().substring(0,10);
        if(createdDate.equals(today)){
            holder.justAdded.setVisibility(View.VISIBLE);
            current.setJustAdded(true);
        }
        if(position == 0){
            holder.initialSeparatorView.setText(datum.get(position).getDate());
            holder.initialSeparatorView.setVisibility(View.VISIBLE);
        } else if(position != datum.size() && !current.getDate().equals(datum.get(position - 1).getDate()) ){ //set separator visible if new date is encountered
            holder.separatorView.setText(datum.get(position).getDate());
            holder.separatorView.setVisibility(View.VISIBLE);
        } else {
            holder.initialSeparatorView.setVisibility(View.GONE);
            holder.separatorView.setVisibility(View.GONE);
        }
            if (current.getName() != null) {
                holder.concertName.setText(current.getName());
            } else {
                holder.concertName.setText(getArtists(current));
            }
        if(current.getVenue().getVenueName() == null) {
            holder.concertVenue.setText(current.getVenueName());
            holder.location.setText(current.getLocation());
        } else {
                holder.concertVenue.setText(current.getVenue().getVenueName());
                holder.location.setText(current.getVenue().getLocation());
                }

            holder.concertVenue.setOnClickListener(v -> collapseArtistList(holder));
            holder.artistPreview.setText(getArtists(current));
            holder.artistPreview.setOnClickListener(v -> collapseArtistList(holder));
            holder.concertName.setOnClickListener(v -> collapseArtistList(holder));

    }

    private void collapseArtistList(RaveViewHolder holder){
        if (isCollapsed) {
            holder.artistPreview.setMaxLines(Integer.MAX_VALUE);
        } else {
            holder.artistPreview.setMaxLines(MAX_LINES_COLLAPSED);
        }
        isCollapsed = !isCollapsed;
    }

    public String getArtists(Datum current){
        String artists = "";
        for(int i = 0; i < current.getArtistList().size(); i++){
            artists +=  current.getArtistList().get(i).getArtistName() + ", ";
        }
        if(artists != "") {
            artists = artists.substring(0, artists.length() - 2);
        }
        return artists;
    }


    void setRaves(List<Datum> raves){
        datum = raves;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if(datum != null){
            return datum.size();
        } else return 0;
    }



}
