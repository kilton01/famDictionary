package com.example.famdictionary.ui.home;

import android.app.ListActivity;
import android.content.Context;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.famdictionary.MainActivity;
import com.example.famdictionary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.zip.Inflater;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private LinearLayout llayout;
    private DatabaseReference ref;
    private List<HashMap<String,String>> list = new ArrayList<>();
    private String meaning,example;
    private TextView wordOfDay,wordMeaning;
    private Button login,signup,signOut;
    private FirebaseUser user;

    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ref = FirebaseDatabase.getInstance().getReference("New Vocab");
        user = FirebaseAuth.getInstance().getCurrentUser();

        wordOfDay = root.findViewById(R.id.WOD);
        wordMeaning = root.findViewById(R.id.word_meaning);
        final SearchView search = root.findViewById(R.id.search);
        final Button add = root.findViewById(R.id.add_word);
        login = root.findViewById(R.id.login);
        signup = root.findViewById(R.id.signup);
        signOut = root.findViewById(R.id.signOut);

        llayout = root.findViewById(R.id.main_layout);

        getWords();

        wordOfDay.setText("Desiree");
        wordMeaning.setText("Beautiful");

        if (user != null){
            login.setVisibility(View.GONE);
            signup.setVisibility(View.GONE);
            signOut.setVisibility(View.VISIBLE);

        }else {
            login.setVisibility(View.VISIBLE);
            signup.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.GONE);

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginScreen.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),SignUpScreen.class);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null){
                    Intent intent = new Intent(getContext(),new_vocab.class);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    Intent intent = new Intent(getContext(),LoginScreen.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });



        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                llayout.removeAllViews();
                String wrd = null;
                if (!s.trim().isEmpty()) {

                    List<HashMap<String, String>> queryList =  doSearch(s.trim());

                    if (queryList.size() > 0) {
                        for (int i = 0; i < queryList.size(); i++) {
                            HashMap<String, String> hash1 = queryList.get(i);
                            wrd = hash1.get("Word");
                            Log.d("logging",wrd);
                            meaning = hash1.get("Meaning");
                            example = hash1.get("Example");

                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            View view = inflater.inflate(R.layout.search_list_view,llayout,false);
                            TextView searchResult = view.findViewById(R.id.search_word);

                            searchResult.setText(wrd);

                            final String wwwd = wrd;
                            searchResult.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(), detailed.class);
                                    intent.putExtra("word", wwwd);
                                    intent.putExtra("meaning", meaning);
                                    intent.putExtra("example", example);
                                    startActivity(intent);

                                }
                            });
                            llayout.addView(view);
                        }
                    }else {
                        LayoutInflater inflater1 = LayoutInflater.from(getContext());
                        View view1 = inflater1.inflate(R.layout.search_list_view,llayout,false);
                        TextView searchResult = view1.findViewById(R.id.search_word);

                        searchResult.setText("Word Not Found");

                        llayout.addView(view1);
                    }
                }else{
                    getWords();
                }
                return false;
            }
        });
        return root;
    }



    public List<HashMap<String, String>> doSearch(String query){
        List<HashMap<String, String>> temp = new ArrayList<>();
        for (HashMap<String, String> current: list){
            String word = current.get("Word");
            if(word.toUpperCase().contains(query.toUpperCase())) {
                temp.add(current);
            }
        }
        return temp;
    }

    public void viewWords(final String newWrd, final String wrdMean, final String wrdEx){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dict_words,llayout,false);
        final TextView DictWord = view.findViewById(R.id.word);

        DictWord.setText(newWrd);

        DictWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),detailed.class);
                intent.putExtra("word",newWrd);
                intent.putExtra("meaning",wrdMean);
                intent.putExtra("example",wrdEx);
                startActivity(intent);

            }
        });
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

                   Collections.sort(list, new  Comparator<HashMap<String, String>>(){
                       public int compare(HashMap<String, String> i1, HashMap<String, String> i2){
                           return i1.get("Word").compareTo(i2.get("Word"));
                       }
                   });
               }
               if (list.size() > 0){
                   for (int i = 0; i < list.size(); i++) {
                       HashMap<String, String> hash = list.get(i);
                       String word = hash.get("Word");
                       meaning = hash.get("Meaning");
                       example = hash.get("Example");

//                       viewWords(word,meaning,example);
                       wordOftheDay();
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

    public static int getRandomRange(int min, int max){
        if (min >= max){
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random random = new Random();
        return random.nextInt((max-min)+1)+min;
    }

    public void wordOftheDay(){
        int r = getRandomRange(0,list.size()-1);
            HashMap<String, String> DaysWord = list.get(r);
            String wordDay = DaysWord.get("Word");
            String wordDayMeaning = DaysWord.get("Meaning");

//            Log.d("Word", wordDay);
//            Log.d("Word", wordDayMeaning);
            wordOfDay.setText(wordDay);
            wordMeaning.setText(wordDayMeaning);

    }


//    public static void refreshFragment(FragmentActivity activity){
//        Fragment fragment = null;
//        fragment = activity.getSupportFragmentManager().findFragmentByTag("HomeFragment");
//        final FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
//        ft.detach(fragment);
//        ft.attach(fragment);
//        ft.commit();
//    }

}