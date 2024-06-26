package com.onesandzeros.patima;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder>{

    private List<Feedback> feedbackList;
    private Context context;
    boolean isSummaey;
    String userName = null, outputImage = null, inputImage = null;
    SQLiteHelper dbHelper;
    int userid;

    public FeedbackAdapter(List<Feedback> feedbackList, Context context, String name, SQLiteHelper dbHelper, boolean isSummaey, int userid) {
        this.feedbackList = feedbackList;
        this.context = context;
        this.userName = name;
        this.dbHelper = dbHelper;
        this.isSummaey = isSummaey;
        this.userid = userid;
    }

    @NonNull
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_viewfeedback, parent, false);
        return new FeedbackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);

        outputImage = dbHelper.getOutputImagepath(feedback.getImg_id());
        inputImage = dbHelper.getInputImagepath(feedback.getImg_id());
        holder.ratingTxt.setText(String.valueOf(feedback.getRating()) + " out of 5");
        holder.usernameTxt.setText(userName);
        String profilepicturePath = dbHelper.getProfilepicture(userid);

        int rating = feedback.getRating();

        if(rating == 1){
            holder.starOne.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
        }else if(rating == 2){
            holder.starOne.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
            holder.starTwo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
        }else if(rating == 3){
            holder.starOne.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
            holder.starTwo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
            holder.starThree.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
        }else if(rating == 4){
            holder.starOne.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
            holder.starTwo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
            holder.starThree.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
            holder.starFour.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
        }else if(rating == 5){
            holder.starOne.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
            holder.starTwo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
            holder.starThree.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
            holder.starFour.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
            holder.starFive.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_rating_filled_small));
        }

        String complete_feedback = feedback.getDesc();
        String[] parts = feedback.getDesc().split("\\$\\$");
        String part1 = parts[0]; // feedbackTxt
        String part2 = parts[1]; // spinnerOneValue
        String part3 = parts[2]; // spinnerTwoValue
        String part4 = parts[3]; // spinnerThreeValue
        holder.feedbackTxt.setText(part1);

        if(isSummaey){
            holder.feedbackImg.setVisibility(View.GONE);
            holder.feedbackuserImg.setVisibility(View.VISIBLE);

            if(profilepicturePath.contains("http")){
                Picasso.get()
                        .load(profilepicturePath)
                        .placeholder(R.drawable.placeholder_profile)
                        .into(holder.feedbackuserImg);
            }else{
                File imageFile = new File(profilepicturePath);
                Picasso.get()
                        .load(imageFile)
                        .placeholder(R.drawable.placeholder_profile)
                        .into(holder.feedbackuserImg);
            }

        }else{
            if(outputImage.contains("http")){
                Picasso.get()
                        .load(outputImage)
                        .placeholder(R.drawable.placeholder_profile)
                        .into(holder.feedbackImg);
            }else{
                File imageFile = new File(outputImage);
                Picasso.get()
                        .load(imageFile)
                        .placeholder(R.drawable.ic_welcome)
                        .into(holder.feedbackImg);
            }

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewSingleFeedbackActivity.class);
                intent.putExtra("imgId", feedback.getImg_id());
                intent.putExtra("base_path", inputImage);
                intent.putExtra("detection_path", outputImage);
                intent.putExtra("timestamp", "Not specified");
                intent.putExtra("feedback", complete_feedback);
                intent.putExtra("feedbackrating", feedback.getRating());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView feedbackImg;
        TextView feedbackTxt, ratingTxt, usernameTxt;
        CircleImageView feedbackuserImg;
        ImageButton starOne, starTwo, starThree, starFour, starFive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            feedbackImg = itemView.findViewById(R.id.feedimg_img);
            feedbackuserImg = itemView.findViewById(R.id.profile_img);
            feedbackTxt = itemView.findViewById(R.id.feedtxt_feedback);
            ratingTxt = itemView.findViewById(R.id.feedtxt_rate);
            usernameTxt = itemView.findViewById(R.id.feedtxt_username);

            starOne = itemView.findViewById(R.id.star_lvl1);
            starTwo = itemView.findViewById(R.id.star_lvl2);
            starThree = itemView.findViewById(R.id.star_lvl3);
            starFour = itemView.findViewById(R.id.star_lvl4);
            starFive = itemView.findViewById(R.id.star_lvl5);
        }
    }
}
