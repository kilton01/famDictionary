package com.example.famdictionary.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.famdictionary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private LinearLayout llayout;
    private DatabaseReference ref;
    private List<HashMap<String,String>> list = new ArrayList<>();
    ArrayList<String> someWordlist = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView wordOfDay = root.findViewById(R.id.WOD);
        final TextView wordMeaning = root.findViewById(R.id.word_meaning);
        final EditText search = root.findViewById(R.id.search);
        final ImageView search_It = root.findViewById(R.id.search_it);
        final Button add = root.findViewById(R.id.add_word);
        llayout = root.findViewById(R.id.main_layout);

        ref = FirebaseDatabase.getInstance().getReference("New Vocab");
        getWords();

        wordOfDay.setText("Desiree");
        wordMeaning.setText("Beautiful");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),new_vocab.class);
                startActivity(intent);
            }
        });

//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    public void viewWords(String newWrd){
        LayoutInflater inflater = LayoutInflater.from(getContext());
//        LayoutInflater inflater1 = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dict_words,llayout,false);
//        View view2 = inflater1.inflate(R.layout.big_letter,llayout,false);
        final TextView DictWord = view.findViewById(R.id.word);
//        final TextView K_letter = view2.findViewById(R.id.K_sort);
        char first_letter = newWrd.charAt(0);

        switch (first_letter){
            case 'A':
                DictWord.setText(newWrd);
                break;
            case 'K':
                DictWord.setText(newWrd);
            default:
                break;
        }

//        llayout.addView(view2);
        llayout.addView(view);
    }

    public void getWords(){

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Data",dataSnapshot.toString());
               list.clear();
               llayout.removeAllViews();
               for (DataSnapshot parent : dataSnapshot.getChildren()){
                   String NewWord = parent.child("New Word").getValue().toString();
                   String WordMeaning = parent.child("Word Meaning").getValue().toString();
                   String WordEx = parent.child("Word Example").getValue().toString();

                   HashMap<String,String> map =  new HashMap<>();
                   map.put("Word",NewWord);
                   map.put("Meaning", WordMeaning);
                   map.put("Example", WordEx);
                   list.add(map);
               }

                someWordlist.clear();
               if (list.size() > 0){
                   for (int i = 0; i < list.size(); i++) {
                       HashMap<String, String> hash = list.get(i);
                       String word = hash.get("Word");
                       someWordlist.add(word);
                       Log.d("ok",someWordlist.get(i));
                       String meaning = hash.get("Meaning");
                       String example = hash.get("Example");

                   }
                    for (int s = 0; s < someWordlist.size(); s++){
                        for (int j = s + 1; j < someWordlist.size(); j++){
                            if (someWordlist.get(s).compareTo(someWordlist.get(j)) > 0){
                                String temp = someWordlist.get(s);
                                someWordlist.set(s,someWordlist.get(j));
                                someWordlist.set(j,temp);

                            }
                        }
                        String wordlist = someWordlist.get(s);
                        viewWords(wordlist);
                        Log.d("word",someWordlist.get(s));
                    }

               }else{
                   Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}