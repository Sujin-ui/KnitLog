package com.example.project_s

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Countertops
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project_s.ui.ProjectListScreen
import com.example.project_s.ui.counter.CounterScreen


sealed class BottomNavItem(val title: String, val route: String) {
//  ⬇️ 버튼의 설정표 ⬇️
    object Counter : BottomNavItem("단수카운터", "counter")
    object Pattern : BottomNavItem("도안", "pattern")
    object Home : BottomNavItem("홈", "home")
    object Community : BottomNavItem("커뮤니티", "community")
    object Profile : BottomNavItem("프로필", "profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }

    val projectList = remember {
        mutableStateListOf(
            ProjectData(
                1, "가을 머플러", "대바늘",
                "메리노 울 · 4.0mm", 47, 120, "진행",
                "20단마다 무늬 바꾸기 🧶"
                )
            )
    }

    var selectedProject by remember { mutableStateOf<ProjectData?>(null) }

    Scaffold(
        bottomBar = {
            NavigationBar {
            // ⬇️ 하단 탭바 순서 설정하는 코드 ⬇️
                val items = listOf(
                    BottomNavItem.Counter,
                    BottomNavItem.Pattern,
                    BottomNavItem.Home,
                    BottomNavItem.Community,
                    BottomNavItem.Profile
                )
                // ⬇️ 버튼을 하나씩 그려주는 일꾼 ⬇️
                items.forEach { item ->
                    NavigationBarItem(
                        selected = selectedTab == item,
                        onClick = {
                            selectedTab = item
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(item.title) },
                        icon = {
                            when (item) {
                            //  하단 탭바 그림 지정해주는 코드
                                BottomNavItem.Home -> Icon(Icons.Default.Home, contentDescription = "홈")
                                BottomNavItem.Counter -> Icon(Icons.Default.Timer, contentDescription = "단수카운터")
                                BottomNavItem.Pattern -> Icon(Icons.Default.List, contentDescription = "도안")
                                BottomNavItem.Community -> Icon(Icons.Default.Share, contentDescription = "커뮤니티")
                                BottomNavItem.Profile -> Icon(Icons.Default.Person, contentDescription = "프로필")
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                Text("메인 홈 화면 (준비 중)", modifier = Modifier.padding(16.dp))
            }

            composable(BottomNavItem.Counter.route) {
                if (selectedProject == null) {
                    ProjectListScreen(
                        // 🧺 기존 있던 단수카운터 불러오기
                        projectList = projectList,
                        // ➕ 단 카운터 추가
                        onAddProject = { newProject -> projectList.add(newProject) },
                        // 🗑️ 단 카운터 삭제
                        onDeleteProject = { projectToDelete -> projectList.remove(projectToDelete) },
                        // 🧺 기존 카운터의 정보들을 불러오는 정보
                        onProjectSelect = { project -> selectedProject = project }
                    )
                } else {
                    CounterScreen(
                        project = selectedProject!!,
                        onBackClick = { selectedProject = null }
                    )
                }
            }

            composable(BottomNavItem.Pattern.route) {
                Text("도안 보관소 화면 (준비 중)", modifier = Modifier.padding(16.dp))
            }

            composable(BottomNavItem.Community.route) {
                Text("커뮤니티 화면 (준비 중)", modifier = Modifier.padding(16.dp))
            }

            composable(BottomNavItem.Profile.route) {
                Text("프로필 화면 (준비 중)", modifier = Modifier.padding(16.dp))
            }
        }
    }
}