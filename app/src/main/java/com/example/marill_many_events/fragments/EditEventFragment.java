//package com.example.marill_many_events.fragments;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import com.example.marill_many_events.R;
//import com.example.marill_many_events.models.Event;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//
//public class EditEventFragment extends Fragment {
//
//    private Button viewParticipantsButton;
//    private String eventDocumentId; // Firebase document ID of the event
//    private Event currentEvent;
//
//    public EditEventFragment() {
//        // Required empty public constructor
//    }
//
//    // Assume that you have a method to set the eventDocumentId when the fragment is created or event is loaded
//    public void setEventDocumentId(String eventDocumentId) {
//        this.eventDocumentId = eventDocumentId;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);
//
//        // Initialize the button
//        viewParticipantsButton = view.findViewById(R.id.view_participants_button);
//
//        // Get the eventDocumentId from arguments or activity
//        if (getArguments() != null) {
//            eventDocumentId = getArguments().getString("eventDocumentId");
//        } else {
//            // Handle the case where eventDocumentId is not passed
//        }
//
//        viewParticipantsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Ensure eventDocumentId is not null
//                if (eventDocumentId != null) {
//
//                    // Create a new instance of EntrantsDrawFragment, passing the eventDocumentId
//                    EntrantsDrawFragment entrantsDrawFragment = EntrantsDrawFragment.newInstance(eventDocumentId);
//
//                    // Replace the current fragment with EntrantsDrawFragment
//                    FragmentManager fragmentManager = getParentFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.fragment_container, entrantsDrawFragment)
//                            .addToBackStack(null)
//                            .commit();
//
//                } else {
//                    // Handle the case where eventDocumentId is null
//                    // You might want to show an error message to the user
//                }
//            }
//        });
//
//        return view;
//    }
//
//
//}
