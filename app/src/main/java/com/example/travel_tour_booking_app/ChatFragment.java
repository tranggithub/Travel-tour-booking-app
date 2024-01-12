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

        // Thêm tin nhắn từ bot vào danh sách
        chatList.add(botMessage);
        chatList.add(botMessageList);


        // Khởi tạo ChatMessageAdapter và gắn với RecyclerView
        chatMessageAdapter = new ChatMessageAdapter(chatList, new ChatMessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String selectItem) {
                addUserMessageToChat(selectItem);
                addBotResponse(selectItem);
            }
        });
        recyclerView.setAdapter(chatMessageAdapter);
        recyclerView.scrollToPosition(chatList.size() - 1);


        return view;
    }

    public String generateBotResponse(String userMessage) {
        if (userMessage.contains("4Travel") && userMessage.contains("đảm bảo") && userMessage.contains("an toàn")) {
            return "Cảm ơn quý khách đã đặt câu hỏi. 4Travel hoạt động dựa trên nguyên tắc lấy lợi ích của khách hàng làm trung tâm, vì thế sự an toàn của khách hàng là một trong những yếu tố mà 4Travel ưu tiên đáp ứng hàng đầu. Do đó quý khách có thể yên tâm sử dụng dịch vụ của chúng tôi.";
        } else if (userMessage.contains("khuyến mãi")) {
            return "Để xem các khuyến mãi, quý khách có thể truy cập trang màn hình chính của chúng tôi hoặc liên hệ với đội ngũ hỗ trợ khách hàng để biết thêm chi tiết.";
        } else if (userMessage.contains("tài khoản ngân hàng")) {
            return "Để liên kết tài khoản ngân hàng, quý khách có thể truy cập phần cài đặt trong ứng dụng hoặc liên hệ với đội ngũ hỗ trợ để được hướng dẫn chi tiết.";
        } else if (userMessage.contains("tốn phí") || userMessage.contains("phí không")) {
            return "Ứng dụng của chúng tôi miễn phí cho việc tải về và sử dụng cơ bản. Tuy nhiên, có thể có một số tính năng hoặc dịch vụ đặc biệt có thể đòi hỏi thanh toán. Quý khách có thể kiểm tra trong phần cài đặt hoặc liên hệ với chúng tôi để biết thêm chi tiết.";
        } else {
            return "Xin lỗi, tôi không hiểu câu hỏi của bạn. Bạn có thể đặt lại câu hỏi một cách khác không?";
        }

    }

    public void addUserMessageToChat(String userMessage) {
        ChatMessage userMessageObj = new ChatMessage(userMessage, 0);

        chatList.add(userMessageObj);

        chatMessageAdapter.notifyDataSetChanged();

        recyclerView.scrollToPosition(chatList.size() - 1);
    }

    public void addBotResponse(String userMessage) {
        if (generateBotResponse(userMessage) != null) {
            ChatMessage botResponse = new ChatMessage(generateBotResponse(userMessage), 1);
            chatList.add(botResponse);
            chatMessageAdapter.notifyDataSetChanged();
        }
    }
}
