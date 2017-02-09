package com.juss.mediaplay.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.juss.mediaplay.entity.QuestionAnswerEntity;
import com.ramo.campuslive.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/7/14.
 */
public class WorkFragment extends ListFragment {


    private ListView class_question_list;


    private List<QuestionAnswerEntity> questionList = new ArrayList<QuestionAnswerEntity>();
    private EditText question_user_content;
    private ImageButton btn_question_into;


    private String TAG = WorkFragment.class.getName();
    private ListView list ;
    private SimpleAdapter adapter;


    /**
     * @描述 在onCreateView中加载布局
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_answer, container,false);
        list = (ListView) view.findViewById(android.R.id.list);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // b = savedInstanceState;
        Log.i(TAG, "--------onCreate");
        QuestionAnswerEntity question1 = new QuestionAnswerEntity();
        question1.setAnswer_content("这道题我理解l ，讲得真好");
        question1.setAnswer_time("2016-08-02");
        question1.setUser_name("sijing");
        questionList.add(question1);
        QuestionAnswerEntity question2 = new QuestionAnswerEntity();
        question1.setAnswer_content("这道题我理解l ，讲得真好");
        question1.setAnswer_time("2016-07-15");
        question1.setUser_name("追逐者");
        questionList.add(question2);

        adapter = new SimpleAdapter(getActivity(), getData(questionList), R.layout.question_answer_item, new String[]{"question_user_name","question_time","question_content"}, new int[]{R.id.answer_user_name,R.id.answer_time,R.id.answer_content});
        setListAdapter(adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        System.out.println(l.getChildAt(position));
        HashMap<String, Object> view= (HashMap<String, Object>) l.getItemAtPosition(position);
        System.out.println(view.get("title").toString()+"+++++++++title");
        Toast.makeText(getActivity(), TAG+l.getItemIdAtPosition(position), Toast.LENGTH_LONG).show();
        System.out.println(v);

        System.out.println(position);


    }




    private List<? extends Map<String, ?>> getData(List<QuestionAnswerEntity> questionList) {
        List<Map<String,Object>> listmap = new ArrayList<>();
        for(QuestionAnswerEntity question:questionList){
            Map<String,Object> item = new HashMap<>();
            item.put("question_user_name",question.getUser_name());
            item.put("question_time",question.getAnswer_time());
            item.put("question_content",question.getAnswer_content());
            listmap.add(item);
        }
        return listmap;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "--------onActivityCreated");

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "----------onAttach");
    }





/*

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QuestionAnswerEntity question1 = new QuestionAnswerEntity();
        question1.setAnswer_content("这道题我理解l ，讲得真好");
        question1.setAnswer_time("2016-04-15");
        question1.setUser_name("sijing");
        questionList.add(question1);
        Toast.makeText(getActivity(), questionList.size(), Toast.LENGTH_LONG).show();
         adapter = new SimpleAdapter(getActivity(), getData(questionList), R.layout.question_answer_item, new String[]{"answer_user_name","answer_time","answer_content"}, new int[]{R.id.answer_user_name,R.id.answer_time,R.id.answer_content});
         setListAdapter(adapter);
    }

    private List<? extends Map<String, ?>> getData(List<QuestionAnswerEntity> questionList) {
        List<Map<String,Object>> listmap = new ArrayList<>();
        for(QuestionAnswerEntity questionanswer:questionList){
            Map<String,Object> item = new HashMap<>();
            item.put("answer_user_name",questionanswer.getUser_name());
            item.put("answer_time",questionanswer.getAnswer_time());
            item.put("answer_content",questionanswer.getAnswer_content());
            listmap.add(item);
        }
        return listmap;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       *//* View homeworkview = inflater.inflate(R.layout.question_answer,container,false);
        class_question_list = (ListView) homeworkview.findViewById(android.R.id.list);
        question_user_content =(EditText) homeworkview.findViewById(R.id.et_answer_content);
        btn_question_into =(ImageButton) homeworkview.findViewById(R.id.btn_answerto);

        btn_question_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = question_user_content.getText().toString();
                if(content!=null && !content.equals("")){
                    Toast.makeText(getActivity(),content,Toast.LENGTH_LONG).show();
                }
            }
        });*//*

        View view = inflater.inflate(R.layout.question_answer, container,false);
        list = (ListView) view.findViewById(android.R.id.list);

        return view;
    }*/
}
