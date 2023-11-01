package com.example.travel_tour_booking_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends Fragment {

    TextView tvDate;
    RecyclerView recyclerView;
    ChatMessageAdapter chatMessageAdapter;
    List<ChatMessage> chatList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        tvDate = view.findViewById(R.id.tv_datetime_chat);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
        String currentDateTime = dateFormat.format(new Date());

        tvDate.setText(currentDateTime);
        tvDate.setVisibility(View.VISIBLE);

        // Khởi tạo RecyclerView và LayoutManager
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // Khởi tạo danh sách tin nhắn
        List<String> strings = new ArrayList<>();
        strings.add("Các câu hỏi thường gặp:");
        strings.add("Xem các khuyến mãi ở đâu?");
        strings.add("Liên kết tài khoản ngân hàng như thế nào?");
        strings.add("4Travel có đảm bảo sự an toàn cho hành khách không?");
        strings.add("Ứng dụng này có tốn phí không?");
        // Tạo tin nhắn từ bot
        ChatMessage botMessage = new ChatMessage("Trung tâm hỗ trợ trực tuyến 4Travel xin chào quý khách! Chúng tôi hỗ trợ trực tuyến cho khách hàng 24/7. Nếu quý khách có thắc mắc gì xin hãy nhập vào ô nhắn tin bên dưới!", 1);
        ChatMessage botMessageList = new ChatMessage(strings, 2);
//        ChatMessage message = new ChatMessage("a",1);
//        ChatMessage message2 = new ChatMessage("a",1);
//        ChatMessage message3 = new ChatMessage("a",0);
//        ChatMessage message4 = new ChatMessage("a", 0);
//        ChatMessage message5 = new ChatMessage("a",0);
//        ChatMessage message6 = new ChatMessage("a",1);
//        ChatMessage message7 = new ChatMessage("a",1);
//        ChatMessage message8 = new ChatMessage("a",0);
//        ChatMessage message9 = new ChatMessage("a", 0);
//        ChatMessage message10 = new ChatMessage("a",0);

        // Thêm tin nhắn từ bot vào danh sách
        chatList.add(botMessage);
        chatList.add(botMessageList);
//        chatList.add(message);
//        chatList.add(message2);
//        chatList.add(message3);
//        chatList.add(message4);
//        chatList.add(message5);
//        chatList.add(message6);
//        chatList.add(message7);
//        chatList.add(message8);
//        chatList.add(message9);
//        chatList.add(message10);


        // Khởi tạo ChatMessageAdapter và gắn với RecyclerView
        chatMessageAdapter = new ChatMessageAdapter(chatList, new ChatMessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String selectItem) {
                addUserMessageToChat(selectItem);
                if (generateBotResponse(selectItem)!=null)
                {
                    ChatMessage botResponse = new ChatMessage(generateBotResponse(selectItem),1);
                    chatList.add(botResponse);
                    chatMessageAdapter.notifyDataSetChanged();
                }
            }
        });
        recyclerView.setAdapter(chatMessageAdapter);
        recyclerView.scrollToPosition(chatList.size()-1);


        return view;
    }
    public String generateBotResponse(String userMessage) {
        if (userMessage.contains("4Travel") && userMessage.contains("đảm bảo") && userMessage.contains("an toàn")) {
            return "Cảm ơn quý khách đã đặt câu hỏi. 4Travel hoạt động dựa trên nguyên tắc lấy lợi ích của khách hàng làm trung tâm, vì thế sự an toàn của khách hàng là một trong những yếu tố mà 4Travel ưu tiên đáp ứng hàng đầu. Do đó quý khách có thể yên tâm sử dụng dịch vụ của chúng tôi.";
        } else {
            // Other condition checks or default responses can be handled here
            // For different types of inquiries or messages
            return "Sorry, I didn't understand that. Can you ask again differently?";
        }
    }
    public void addUserMessageToChat(String userMessage) {
        ChatMessage userMessageObj = new ChatMessage(userMessage, 0);

        chatList.add(userMessageObj);

        chatMessageAdapter.notifyDataSetChanged();

        recyclerView.scrollToPosition(chatList.size() - 1);
    }
}
