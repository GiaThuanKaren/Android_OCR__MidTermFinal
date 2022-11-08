package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ocrextracttext.MainActivity;
import com.example.ocrextracttext.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.TextRecognitionList;

public class TextRecognitionAdapter extends RecyclerView.Adapter<TextRecognitionAdapter.TextRecognitionViewHolder> {
    private MainActivity context;
    private List<TextRecognitionList> textRecognitionLists;
    private LayoutInflater inflater;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
    public SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-M-yyyy", Locale.US);
    Date date = null;
    String outputDateString = null;

    public TextRecognitionAdapter(MainActivity context, List<TextRecognitionList> textRecognitionLists) {
        this.context = context;
        this.textRecognitionLists = textRecognitionLists;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public TextRecognitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_todolist, parent, false);
        return new TextRecognitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextRecognitionViewHolder holder, int position) {
        TextRecognitionList text = textRecognitionLists.get(position);
        holder.header.setText(text.get_header());
        holder.description.setText(text.get_description());
        holder.day.setText(text.get_day());
        holder.date.setText(text.get_date());
        holder.month.setText(text.get_month());

        try {
            date = inputDateFormat.parse(text.get_date());
            outputDateString = dateFormat.format(date);

            String[] items1 = outputDateString.split(" ");
            String day = items1[0];
            String dd = items1[1];
            String month = items1[2];

            holder.day.setText(day);
            holder.date.setText(dd);
            holder.month.setText(month);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return textRecognitionLists.size();
    }

    class TextRecognitionViewHolder extends RecyclerView.ViewHolder {
        private TextView day, date, month, header, description;
        public TextRecognitionViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header);
            description = itemView.findViewById(R.id.description);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            month = itemView.findViewById(R.id.month);
        }
    }
}
