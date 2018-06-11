package za.co.samtakie.samtakieradio.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;


import za.co.samtakie.samtakieradio.ui.ItemFragment.*;
import za.co.samtakie.samtakieradio.ui.MainActivity;
import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.ui.MainFragment;
import za.co.samtakie.samtakieradio.ui.MainFragment.RadioAdapterOnClickHandler;

/***
 * Created by Jurgen Emanuels on the 02/05/2018
 * {@link RadioAdapter} exposes a list of radio stations to a
 * {@link RecyclerView}
 */
public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.RadioViewHolder>{


    private ArrayList<Radio> mRadioData;
    private final Context mContext;
    private Cursor cursor;

    /* An on-click handler that we've defined in MainActivity to make it easy for an Activity
     * to interface with the RecyclerView */
    private final RadioAdapterOnClickHandler mClickHandler;

    /***
     * Creates a RadioAdapter object
     * @param context provide access to application data
     * @param mClickHandler The on-click handler for this adapter. This single handler is called
     *                      when an item is clicked.
     */
    public RadioAdapter(Context context, RadioAdapterOnClickHandler mClickHandler){
        this.mContext = context;
        this.mClickHandler = mClickHandler;
    }

    /*public interface RadioAdapterOnClickHandler {
        void radioItemOnClickHandler(int radioID, View view, int adapterPosition, String radioLink, String radioName, String radioImage);
    }*/

    /***
     * This gets called when each ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within
     * @param viewType If your RecyclerView has more than one type of item(which ours doesn't) you
     *                 can use this viewType integer tp provide a different layout. See
     *                 {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                 for more details
     * @return         A new RadioViewHolder that holds the View for each list item.
     */
    @NonNull
    @Override
    public RadioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParent = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParent);
        return new RadioViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RadioViewHolder holder, int position) {
        cursor.moveToPosition(position);
        String radioImgLink = cursor.getString(MainActivity.INDEX_COLUMN_ONLINE_RADIO_IMAGE);
        String samtakieUrl = "http://www.samtakie.co.za/img/samtakie_radio/";
        String imgRadioUrl = samtakieUrl + radioImgLink +".jpg";
        String radio = cursor.getString(MainActivity.INDEX_COLUMN_ONLINE_RADIO_NAME);
        holder.mRadioNameText.setText(radio);
        Picasso.get()
                .load(Uri.parse(imgRadioUrl))
                .resize(400, 400)
                .placeholder(R.drawable.main_background)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        assert holder.radioImageView != null;

                        holder.radioImageView.setImageBitmap(bitmap);
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(@NonNull Palette palette) {
                                        Palette.Swatch textSwatch = palette.getDarkVibrantSwatch();
                                        //Palette.Swatch bgSwatch = palette.getDarkVibrantSwatch();
                                        if(textSwatch != null) {
                                            //Toast.makeText(mContext, "Null swatch :(", Toast.LENGTH_LONG).show();
                                            holder.mRadioNameText.setBackgroundColor(textSwatch.getRgb());
                                            holder.crRadio.setCardBackgroundColor(textSwatch.getRgb());
                                            holder.mRadioNameText.setTextColor(textSwatch.getBodyTextColor());
                                            return;
                                        }



                                        /*if(bgSwatch != null){
                                            holder.subHeadText.setBackgroundColor(bgSwatch.getRgb());
                                            holder.subHeadText.setTextColor(bgSwatch.getBodyTextColor());
                                            return;
                                        }*/



                                    }
                                });

                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        Log.d("Check image: ", imgRadioUrl);
    }

    @Override
    public int getItemCount() {
        if(cursor == null){
            return 0;
        }
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        cursor = newCursor;
        notifyDataSetChanged();
    }


    public class RadioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mRadioNameText;
        private ImageView radioImageView;
        private TextView subHeadText;
        private CardView crRadio;

        public RadioViewHolder(View itemView) {
            super(itemView);
            mRadioNameText = (TextView)itemView.findViewById(R.id.radioName);
            radioImageView = (ImageView) itemView.findViewById(R.id.radioImage);
            crRadio = (CardView) itemView.findViewById(R.id.cv_radio);

            //subHeadText = (TextView)itemView.findViewById(R.id.subHead);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            cursor.moveToPosition(adapterPosition);
            int radioID = cursor.getInt(MainActivity.INDEX_COLUMN_ONLINE_RADIO_ID);
            String radioLink = cursor.getString(MainActivity.INDEX_COLUMN_ONLINE_RADIO_LINK);
            String radioName = cursor.getString(MainActivity.INDEX_COLUMN_ONLINE_RADIO_NAME);
            String radioImage = cursor.getString(MainActivity.INDEX_COLUMN_ONLINE_RADIO_IMAGE);
            mClickHandler.radioItemOnClickHandler(radioID, view, adapterPosition, radioLink, radioName, radioImage);
        }
    }
}