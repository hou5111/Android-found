package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ContactsFragment extends Fragment {

    private EditText etSearch;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private ImageButton btnClearSearch;
    private TextView tvSearchHint;
    private ContactSectionAdapter adapter;

    private List<Contact> originalContacts = new ArrayList<>();
    private List<Section> sectionList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 使用原来的通讯录布局
        View view = inflater.inflate(R.layout.activity_main, container, false);

        // 初始化视图
        etSearch = view.findViewById(R.id.etSearch);
        recyclerView = view.findViewById(R.id.recyclerView);
        fabAdd = view.findViewById(R.id.fabAdd);
        btnClearSearch = view.findViewById(R.id.btnClearSearch);
        tvSearchHint = view.findViewById(R.id.tvSearchHint);

        setupRecyclerView();
        setupSearch();
        setupFab();
        loadContacts();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(null);

        adapter = new ContactSectionAdapter(new ContactSectionAdapter.OnContactClickListener() {
            @Override
            public void onContactClick(Contact contact) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("contact", contact);
                startActivityForResult(intent, 100);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                filterContacts(text);
                btnClearSearch.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                filterContacts("");
                btnClearSearch.setVisibility(View.GONE);
                tvSearchHint.setVisibility(View.GONE);
            }
        });
    }

    private void setupFab() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                startActivityForResult(intent, 200);
            }
        });
    }

    private void loadContacts() {
        // 加载测试数据
        originalContacts.clear();
        originalContacts.add(new Contact(1, "张三", "13800138000", "男", "大学同学"));
        originalContacts.add(new Contact(2, "李四", "13900139000", "女", "同事"));
        originalContacts.add(new Contact(3, "王五", "13700137000", "男", "家人"));
        originalContacts.add(new Contact(4, "赵六", "13600136000", "女", "朋友"));
        originalContacts.add(new Contact(5, "孙七", "13500135000", "男", "客户"));

        filterContacts("");
    }

    private void filterContacts(String keyword) {
        List<Contact> filtered = new ArrayList<>();

        if (TextUtils.isEmpty(keyword)) {
            filtered.addAll(originalContacts);
            tvSearchHint.setVisibility(View.GONE);
        } else {
            String lowerKeyword = keyword.toLowerCase();
            for (Contact c : originalContacts) {
                if (c.getName().toLowerCase().contains(lowerKeyword) ||
                        c.getPhone().contains(keyword)) {
                    filtered.add(c);
                }
            }

            if (filtered.isEmpty()) {
                tvSearchHint.setText("未找到匹配 '" + keyword + "' 的联系人");
                tvSearchHint.setVisibility(View.VISIBLE);
            } else {
                tvSearchHint.setText("找到 " + filtered.size() + " 个匹配联系人");
                tvSearchHint.setVisibility(View.VISIBLE);
            }
        }

        sortAndGroupContacts(filtered);
    }

    private void sortAndGroupContacts(List<Contact> contacts) {
        Collections.sort(contacts, (c1, c2) -> {
            String l1 = c1.getFirstLetter();
            String l2 = c2.getFirstLetter();
            if ("#".equals(l1)) return 1;
            if ("#".equals(l2)) return -1;
            return l1.compareTo(l2);
        });

        sectionList.clear();
        Map<String, List<Contact>> groupMap = new TreeMap<>();
        for (Contact c : contacts) {
            String letter = c.getFirstLetter();
            if (!groupMap.containsKey(letter)) {
                groupMap.put(letter, new ArrayList<>());
            }
            groupMap.get(letter).add(c);
        }

        for (Map.Entry<String, List<Contact>> entry : groupMap.entrySet()) {
            sectionList.add(new Section(entry.getKey(), entry.getValue()));
        }

        if (adapter != null) {
            adapter.setSections(sectionList);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && data != null) {
            if (requestCode == 100 && data.hasExtra("deleteId")) {
                int delId = data.getIntExtra("deleteId", -1);
                Iterator<Contact> iterator = originalContacts.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getId() == delId) {
                        iterator.remove();
                        break;
                    }
                }
                filterContacts(etSearch.getText().toString());
            }
            else if (requestCode == 200 && data.hasExtra("newContact")) {
                Contact newContact = (Contact) data.getSerializableExtra("newContact");
                if (newContact != null) {
                    newContact.setId(getNextContactId());
                    originalContacts.add(newContact);
                    Toast.makeText(getContext(), "联系人添加成功", Toast.LENGTH_SHORT).show();
                    filterContacts(etSearch.getText().toString());
                }
            }
        }
    }

    private int getNextContactId() {
        int maxId = 0;
        for (Contact contact : originalContacts) {
            if (contact.getId() > maxId) {
                maxId = contact.getId();
            }
        }
        return maxId + 1;
    }
}