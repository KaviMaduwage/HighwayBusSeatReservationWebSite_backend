package com.project.seatReservation.controller;

import com.project.seatReservation.model.FoundItem;
import com.project.seatReservation.model.LostItem;
import com.project.seatReservation.service.LostFoundService;
import com.project.seatReservation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class LostFoundController {

    LostFoundService lostFoundService;

    UserService userService;

    public LostFoundController(LostFoundService lostFoundService, UserService userService) {
        this.lostFoundService = lostFoundService;
        this.userService = userService;
    }

    @RequestMapping(value = "/saveFoundItem",method = RequestMethod.POST)
    public ResponseEntity<?> saveFoundItem(@RequestBody FoundItem foundItem){

        String message = "Successfully saved.";
        lostFoundService.saveFoundItem(foundItem);

        return ResponseEntity.ok().body(message);

    }

    @RequestMapping(value = "/saveLostItem",method = RequestMethod.POST)
    public ResponseEntity<?> saveLostItem(@RequestBody LostItem lostItem){

        String message = "Successfully saved.";
        lostFoundService.saveLostItem(lostItem);

        return ResponseEntity.ok().body(message);

    }

    @RequestMapping(value = "/loadLostItems",method = RequestMethod.POST)
    public ResponseEntity<?> loadLostItems(){

        List<LostItem> lostItemList = new ArrayList<>();
        lostItemList = lostFoundService.loadLostItems();

        return ResponseEntity.ok().body(lostItemList.toArray());

    }

    @RequestMapping(value = "/loadFoundItems",method = RequestMethod.POST)
    public ResponseEntity<?> loadFoundItems(){

        List<FoundItem> foundItemList = new ArrayList<>();
        foundItemList = lostFoundService.loadFoundItems();

        return ResponseEntity.ok().body(foundItemList.toArray());

    }

    @RequestMapping(value = "/getLostItemsAfterDate",method = RequestMethod.POST)
    public ResponseEntity<?> getLostItemsAfterDate(@RequestBody Map<String,String> requestBody){
        List<LostItem> lostItemList = new ArrayList<>();

        String searchDate = requestBody.get("searchDate");
        lostItemList = lostFoundService.getLostItemsAfterDate(searchDate);
        return ResponseEntity.ok().body(lostItemList.toArray());

    }

    @RequestMapping(value = "/getFoundItemsAfterDate",method = RequestMethod.POST)
    public ResponseEntity<?> getFoundItemsAfterDate(@RequestBody Map<String,String> requestBody){
        List<FoundItem> foundItemList = new ArrayList<>();

        String searchDate = requestBody.get("searchDate");
        foundItemList = lostFoundService.getFoundItemsAfterDate(searchDate);
        return ResponseEntity.ok().body(foundItemList.toArray());

    }

    @RequestMapping(value = "/getUserLostItemPosts", method = RequestMethod.POST)
    public ResponseEntity<?> getUserLostItemPosts(@RequestBody Map<String,Integer> requestBody){

        List<LostItem> lostItemList = new ArrayList<>();
        int userId = requestBody.get("userId");
        lostItemList = lostFoundService.getLostItemsByUserId(userId);
        return ResponseEntity.ok().body(lostItemList.toArray());
    }

    @RequestMapping(value = "/getUserFoundItemPosts", method = RequestMethod.POST)
    public ResponseEntity<?> getUserFoundItemPosts(@RequestBody Map<String,Integer> requestBody){

        List<FoundItem> foundItemList = new ArrayList<>();
        int userId = requestBody.get("userId");
        foundItemList = lostFoundService.getFoundItemsByUserId(userId);
        return ResponseEntity.ok().body(foundItemList.toArray());
    }

    @RequestMapping(value = "/deleteUserFoundPost",method = RequestMethod.POST)
    public ResponseEntity<?> deleteUserFoundPost(@RequestBody Map<String,Integer> requestBody){

        int userId = requestBody.get("userId");
        int foundItemId = requestBody.get("foundItemId");

        List<FoundItem> foundItemList = lostFoundService.findFoundItemById(foundItemId);

        String message = "";
        if(foundItemList != null && foundItemList.size() > 0 && foundItemList.get(0).getUser().getUserId() == userId){
            lostFoundService.deleteFoundItemById(foundItemList.get(0));
            message = "Successfully deleted the found item post.";
        }else {
            message = "Can't delete the post.";
        }

        return ResponseEntity.ok().body(message);
    }

    @RequestMapping(value = "/deleteUserLostPost",method = RequestMethod.POST)
    public ResponseEntity<?> deleteUserLostPost(@RequestBody Map<String,Integer> requestBody){

        int userId = requestBody.get("userId");
        int lostItemId = requestBody.get("lostItemId");

        List<LostItem> lostItemList = lostFoundService.findLostItemById(lostItemId);

        String message = "";
        if(lostItemList != null && lostItemList.size() > 0 && lostItemList.get(0).getUser().getUserId() == userId){
            lostFoundService.deleteLostItemById(lostItemList.get(0));
            message = "Successfully deleted the lost item post.";
        }else {
            message = "Can't delete the post.";
        }

        return ResponseEntity.ok().body(message);
    }
}
