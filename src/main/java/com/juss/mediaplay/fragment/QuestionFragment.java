package com.juss.mediaplay.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.juss.mediaplay.entity.Question;
import com.ramo.campuslive.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/7/14.
 */
public class QuestionFragment extends ListFragment {

    private ListView class_question_list;
    private SimpleAdapter myAdapter;


    private List<Question> questionList = new ArrayList<Question>();
    private EditText question_user_content;
    private ImageButton btn_question_into;


    private int what = 1;
    // private SimpleAdapter adapter;


    private String TAG = QuestionFragment.class.getName();
    private ListView list;
    private SimpleAdapter adapter;


    /**
     * @描述 在onCreateView中加载布局
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_homework_question, container, false);
        list = (ListView) view.findViewById(android.R.id.list);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // b = savedInstanceState;
        // Log.i(TAG, "--------onCreate");
        initQuestionList();

    }

    private void initQuestionList() {
        Question question1 = new Question();
        question1.setQuestion_content("这道题我不是很理解，可以再讲一遍吗？+");
        question1.setQuestion_time("2016-04-15");
        question1.setQuestion_user_name("sijing");
        questionList.add(question1);
        Question question2 = new Question();
        question1.setQuestion_content("这道题我不是很理解，可以再讲一遍吗？+");
        question1.setQuestion_time("2016-04-15");
        question1.setQuestion_user_name("sijing");
        questionList.add(question2);

        adapter = new SimpleAdapter(getActivity(), getData(questionList), R.layout.question_answer_item, new String[]{"question_user_name", "question_time", "question_content"}, new int[]{R.id.question_user_name, R.id.question_time, R.id.question_content});
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

      /*  System.out.println(l.getChildAt(position));
        HashMap<String, Object> view = (HashMap<String, Object>) l.getItemAtPosition(position);
        System.out.println(view.get("title").toString() + "+++++++++title");
        // Toast.makeText(getActivity(), TAG+l.getItemIdAtPosition(position), Toast.LENGTH_LONG).show();
        System.out.println(v);

        System.out.println(position);*/



    }


    private List<? extends Map<String, ?>> getData(String[] strs) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < strs.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", strs[i]);
            list.add(map);

        }

        return list;
    }

    private List<? extends Map<String, ?>> getData(List<Question> questionList) {
        List<Map<String, Object>> listmap = new ArrayList<>();
        for (Question question : questionList) {
            Map<String, Object> item = new HashMap<>();
            item.put("question_user_name", question.getQuestion_user_name());
            item.put("question_time", question.getQuestion_time());
            item.put("question_content", question.getQuestion_content());
            listmap.add(item);
        }
        return listmap;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Log.i(TAG, "--------onActivityCreated");

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Log.i(TAG, "----------onAttach");
    }


}
