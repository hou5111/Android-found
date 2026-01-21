package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ContactSectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 两种Item类型：字母标题、联系人
    private static final int TYPE_LETTER = 0;
    private static final int TYPE_CONTACT = 1;

    // 存储所有item（字母或联系人对象）
    private List<Object> itemList = new ArrayList<>();
    private OnContactClickListener listener;

    // 点击回调接口
    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    public ContactSectionAdapter(OnContactClickListener listener) {
        this.listener = listener;
    }

    // 安全地更新数据
    public void setSections(List<Section> sections) {
        // 清空现有数据
        itemList.clear();

        if (sections != null && !sections.isEmpty()) {
            for (Section section : sections) {
                // 添加字母标题
                itemList.add(section.getLetter());

                // 添加该字母下的所有联系人
                if (section.getContacts() != null) {
                    itemList.addAll(section.getContacts());
                }
            }
        }

        // 使用notifyDataSetChanged确保一致性
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < itemList.size()) {
            Object item = itemList.get(position);
            return (item instanceof String) ? TYPE_LETTER : TYPE_CONTACT;
        }
        return TYPE_CONTACT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_LETTER) {
            View view = inflater.inflate(R.layout.item_letter, parent, false);
            return new LetterViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_contact, parent, false);
            return new ContactViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < 0 || position >= itemList.size()) {
            return;
        }

        Object item = itemList.get(position);

        if (holder instanceof LetterViewHolder) {
            LetterViewHolder letterHolder = (LetterViewHolder) holder;
            letterHolder.tvLetter.setText((String) item);
        } else if (holder instanceof ContactViewHolder) {
            ContactViewHolder contactHolder = (ContactViewHolder) holder;
            Contact contact = (Contact) item;
            contactHolder.tvName.setText(contact.getName());
            contactHolder.avatar.setName(contact.getName());

            // 点击事件
            contactHolder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onContactClick(contact);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // 字母标题ViewHolder
    static class LetterViewHolder extends RecyclerView.ViewHolder {
        TextView tvLetter;
        public LetterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLetter = itemView.findViewById(R.id.tvLetter);
        }
    }

    // 联系人ViewHolder
    static class ContactViewHolder extends RecyclerView.ViewHolder {
        CircleTextAvatar avatar;
        TextView tvName;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}