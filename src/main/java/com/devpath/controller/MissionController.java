package com.devpath.controller;

import com.devpath.entity.PlacementMission;
import com.devpath.entity.StudentMissionProgress;
import com.devpath.entity.User;
import com.devpath.service.AuthService;
import com.devpath.service.MissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MissionController {

    private final MissionService missionService;
    private final AuthService authService;

    public MissionController(MissionService missionService, AuthService authService) {
        this.missionService = missionService;
        this.authService = authService;
    }

    @GetMapping("/missions")
    public ResponseEntity<List<PlacementMission>> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @GetMapping("/missions/{id}")
    public ResponseEntity<PlacementMission> getMissionById(@PathVariable Long id) {
        return ResponseEntity.ok(missionService.getMissionById(id));
    }

    @GetMapping("/student/missions")
    public ResponseEntity<List<StudentMissionProgress>> getStudentMissions() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(missionService.getStudentMissions(user.getId()));
    }

    @PostMapping("/student/missions/{id}/start")
    public ResponseEntity<StudentMissionProgress> startMission(@PathVariable Long id) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(missionService.startMission(user, id));
    }

    @PostMapping("/student/missions/{id}/tasks/{taskId}/complete")
    public ResponseEntity<StudentMissionProgress> completeTask(@PathVariable Long id, @PathVariable Long taskId) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(missionService.completeTask(user, id, taskId));
    }
}
