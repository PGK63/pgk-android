package ru.pgk63.feature_raportichka.screens.raportichkaScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.pgk63.core_model.group.Group
import ru.pgk63.core_common.api.raportichka.model.Raportichka
import ru.pgk63.core_common.common.response.Result
import ru.pgk63.core_common.enums.user.UserRole
import ru.pgk63.core_common.extension.launchWhenStarted
import ru.pgk63.core_common.extension.parseToBaseDateFormat
import ru.pgk63.core_database.user.model.UserLocalDatabase
import ru.pgk63.core_model.raportichka.RaportichkaRow
import ru.pgk63.core_ui.R
import ru.pgk63.core_ui.theme.PgkTheme
import ru.pgk63.core_ui.view.EmptyUi
import ru.pgk63.core_ui.view.ErrorUi
import ru.pgk63.feature_raportichka.screens.raportichkaScreen.model.AddRaportichkaMenu
import ru.pgk63.feature_raportichka.screens.raportichkaScreen.model.RaportichkaRowMenu
import ru.pgk63.feature_raportichka.screens.raportichkaScreen.model.RaportichkaSheetType
import ru.pgk63.feature_raportichka.screens.raportichkaScreen.viewModel.RaportichkaViewModel
import ru.pgk63.feature_raportichka.view.RaportichkaTable
import ru.pgk63.core_ui.view.SortingItem

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun RaportichkaRoute(
    viewModel: RaportichkaViewModel = hiltViewModel(),
    confirmation: Boolean? = null,
    onlyDate: String? = null,
    startDate: String? = null,
    endDate: String? = null,
    groupIds: List<Int>? = null,
    subjectIds: List<Int>? = null,
    classroomTeacherIds: List<Int>? = null,
    numberLessons: List<Int>? = null,
    teacherIds: List<Int>? = null,
    studentIds: List<Int>? = null,
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId: Int) -> Unit,
    onTeacherDetailsScreen: (teacherId: Int) -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit,
    onRaportichkaUpdateRowScreen: (
        raportichkaId: Int,
        groupId: Int,
        rowId: Int,
        teacherId: Int,
        updateNumberLesson: String,
        updateCountHours: String,
        updateStudentId: Int,
        updateSubjectId: Int
    ) -> Unit,
    onRaportichkaAddRowScreen: (raportichkaId: Int, groupId: Int) -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val pagerState = rememberPagerState()

    var user by remember { mutableStateOf(UserLocalDatabase()) }
    val groups = viewModel.responseGroup.collectAsLazyPagingItems()

    val raportichkaList = viewModel.responseRaportichkaList.collectAsLazyPagingItems()

    var responseAddOrDeleteRow by remember { mutableStateOf<Result<Unit?>?>(null) }

    viewModel.user.onEach { userLocalDatabase ->
        user = userLocalDatabase
    }.launchWhenStarted()

    viewModel.responseAddOrDeleteRow.onEach { result ->
        responseAddOrDeleteRow = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = responseAddOrDeleteRow, block = {
        when(responseAddOrDeleteRow){
            is Result.Error -> {
                scaffoldState.snackbarHostState.showSnackbar(message = context.getString(R.string.error))
                viewModel.responseDeleteRowToNull()
            }
            is Result.Loading -> Unit
            is Result.Success -> {
                raportichkaList.refresh()

                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.success)
                )
                viewModel.responseDeleteRowToNull()
            }
            null -> Unit
        }
    })

    LaunchedEffect(Unit){
        viewModel.getRaportichka(
            confirmation = confirmation,
            onlyDate = onlyDate,
            startDate = startDate,
            endDate = endDate,
            groupIds = groupIds,
            subjectIds = subjectIds,
            classroomTeacherIds = classroomTeacherIds,
            numberLessons = numberLessons,
            teacherIds = teacherIds,
            studentIds = studentIds
        )
    }

    RaportichkaScreen(
        scaffoldState = scaffoldState,
        modalBottomSheetState = modalBottomSheetState,
        pagerState = pagerState,
        raportichkaList = raportichkaList,
        user = user,
        searchByStudentId = studentIds,
        searchByGroupsId = groupIds,
        searchBySubjectsId = subjectIds,
        searchByTeacherId = teacherIds,
        onBackScreen = onBackScreen,
        onStudentDetailsScreen = onStudentDetailsScreen,
        onTeacherDetailsScreen = onTeacherDetailsScreen,
        onRaportichkaAddRow = onRaportichkaAddRowScreen,
        onSubjectDetailsScreen = onSubjectDetailsScreen,
        updateRow = onRaportichkaUpdateRowScreen,
        deleteRow = { rowId ->
            viewModel.deleteRow(rowId)
        },
        getGroup = { search ->
            viewModel.getGroups(search = search)
            groups
        },
        addRaportichka = { groupId ->
            if(user.userRole == UserRole.HEADMAN || user.userRole == UserRole.DEPUTY_HEADMAN){
                viewModel.createRaportichka()
            }else if(groupId != null) {
                viewModel.createRaportichka(groupId)
            }
        },
        onRaportichkaUpdateConfirmation = {
            viewModel.updateConfirmation(it)
        }
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
private fun RaportichkaScreen(
    scaffoldState: ScaffoldState,
    modalBottomSheetState: ModalBottomSheetState,
    pagerState: PagerState,
    raportichkaList: LazyPagingItems<Raportichka>,
    user: UserLocalDatabase,
    searchByStudentId: List<Int>? = null,
    searchByGroupsId: List<Int>? = null,
    searchBySubjectsId: List<Int>? = null,
    searchByTeacherId: List<Int>? = null,
    getGroup: (search:String?) -> LazyPagingItems<Group>,
    addRaportichka: (groupId: Int?) -> Unit,
    onBackScreen: () -> Unit,
    onStudentDetailsScreen: (studentId:Int) -> Unit,
    onTeacherDetailsScreen: (teacherId: Int) -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit,
    onRaportichkaAddRow: (raportichkaId:Int,groupId:Int) -> Unit,
    onRaportichkaUpdateConfirmation: (rowId: Int) -> Unit,
    updateRow: (
        raportichkaId: Int,
        groupId: Int,
        rowId: Int,
        teacherId: Int,
        updateNumberLesson: String,
        updateCountHours: String,
        updateStudentId: Int,
        updateSubjectId: Int
    ) -> Unit,
    deleteRow: (rowId: Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    var raportichkaSheetType by remember { mutableStateOf<RaportichkaSheetType?>(null) }

    LaunchedEffect(key1 = raportichkaSheetType, block = {
        if(raportichkaSheetType != null){
            modalBottomSheetState.show()
        }else {
            modalBottomSheetState.hide()
        }
    })

    LaunchedEffect(key1 = modalBottomSheetState.isVisible, block = {
        if(!modalBottomSheetState.isVisible){
            raportichkaSheetType = null
        }
    })

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = PgkTheme.colors.primaryBackground,
        topBar = {
            TopAppBar(
                backgroundColor = PgkTheme.colors.primaryBackground,
                title = {

                    val text = if(raportichkaList.itemCount > 0 && raportichkaList[pagerState.currentPage] != null)
                        raportichkaList[pagerState.currentPage]!!.date.parseToBaseDateFormat()
                    else
                        stringResource(id = R.string.raportichka)

                    Text(
                        text = text,
                        color = PgkTheme.colors.primaryText,
                        style = PgkTheme.typography.toolbar,
                        fontFamily = PgkTheme.fontFamily.fontFamily
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackScreen) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = PgkTheme.colors.primaryText
                        )
                    }
                },
                actions = {
                    AnimatedVisibility(visible = pagerState.currentPage != 0) {
                        IconButton(onClick = {
                            if(pagerState.currentPage != 0){
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage-1)
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = null,
                                tint = PgkTheme.colors.tintColor
                            )
                        }
                    }

                    AnimatedVisibility(visible = pagerState.currentPage != (raportichkaList.itemCount-1)) {
                        IconButton(onClick = {
                            if(pagerState.currentPage != (raportichkaList.itemCount-1)){
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage+1)
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = null,
                                tint = PgkTheme.colors.tintColor
                            )
                        }
                    }

                    AnimatedVisibility(visible = raportichkaList.itemCount > 0) {
                        IconButton(
                            onClick = {
                                raportichkaSheetType = RaportichkaSheetType.AddRaportichkaMenu
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = PgkTheme.colors.tintColor
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { state ->
            SnackbarHost(hostState = state) { data ->
                Snackbar(
                    backgroundColor = PgkTheme.colors.secondaryBackground,
                    contentColor = PgkTheme.colors.primaryText,
                    shape = PgkTheme.shapes.cornersStyle,
                    snackbarData = data
                )
            }
        },
        content = { paddingValues ->
            if (
                raportichkaList.itemCount <= 0 && raportichkaList.loadState.refresh !is LoadState.Loading
            ){
                EmptyUi()
            }else if(raportichkaList.loadState.refresh is LoadState.Error) {
                ErrorUi()
            }else{
                ModalBottomSheetLayout(
                    sheetState = modalBottomSheetState,
                    sheetBackgroundColor = PgkTheme.colors.secondaryBackground,
                    sheetShape = PgkTheme.shapes.cornersStyle,
                    sheetContent = {

                        val raportichka = if(raportichkaList.itemCount > 0)
                            raportichkaList[pagerState.currentPage]!!
                        else
                            null

                        SheetContent(
                            type = raportichkaSheetType,
                            raportichka = raportichka,
                            user = user,
                            onStudentDetailsScreen = onStudentDetailsScreen,
                            onTeacherDetailsScreen = onTeacherDetailsScreen,
                            onSubjectDetailsScreen = onSubjectDetailsScreen,
                            onRaportichkaAddRow = onRaportichkaAddRow,
                            onRaportichkaUpdateConfirmation = onRaportichkaUpdateConfirmation,
                            deleteRow = deleteRow,
                            updateRow = updateRow,
                            getGroup = getGroup,
                            addRaportichka = {
                                addRaportichka(it)
                                raportichkaSheetType = null
                            },
                            onClickAddRaportichkaMenu = {
                                raportichkaSheetType = if(
                                    user.userRole == UserRole.DEPUTY_HEADMAN || user.userRole == UserRole.HEADMAN
                                ){
                                    addRaportichka(null)
                                    null
                                }else {
                                    RaportichkaSheetType.AddRaportichka
                                }
                            }
                        )
                    }
                ){
                    RaportichkaList(
                        paddingValues = paddingValues,
                        raportichkaList = raportichkaList,
                        searchByGroupsId = searchByGroupsId,
                        searchByStudentId = searchByStudentId,
                        searchBySubjectsId = searchBySubjectsId,
                        searchByTeacherId = searchByTeacherId,
                        pagerState = pagerState,
                        onClickRow = { row ->
                            raportichkaSheetType = RaportichkaSheetType.RaportichkaRowMenu(row)
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun SheetContent(
    type: RaportichkaSheetType?,
    raportichka: Raportichka?,
    user: UserLocalDatabase,
    getGroup: (search:String?) -> LazyPagingItems<Group>,
    addRaportichka: (groupId: Int) -> Unit,
    onStudentDetailsScreen: (studentId:Int) -> Unit,
    onTeacherDetailsScreen: (teacherId: Int) -> Unit,
    onSubjectDetailsScreen: (subjectId: Int) -> Unit,
    onRaportichkaAddRow: (raportichkaId:Int,groupId:Int) -> Unit,
    onRaportichkaUpdateConfirmation: (rowId: Int) -> Unit,
    updateRow: (
        raportichkaId: Int,
        groupId: Int,
        rowId: Int,
        teacherId: Int,
        updateNumberLesson: String,
        updateCountHours: String,
        updateStudentId: Int,
        updateSubjectId: Int
    ) -> Unit,
    deleteRow: (rowId: Int) -> Unit,
    onClickAddRaportichkaMenu: () -> Unit
) {
    when(type){
        is RaportichkaSheetType.RaportichkaRowMenu -> {

            val visibleDeleteButton: Boolean = if(user.userRole != null){
                when (user.userRole) {
                    UserRole.ADMIN -> true
                    UserRole.TEACHER -> {
                        type.row.teacher.id == (user.userId ?: 0)
                    }
                    UserRole.DEPUTY_HEADMAN, UserRole.HEADMAN -> {
                        !type.row.confirmation && (raportichka?.group?.headman?.id == user.userId ||
                                raportichka?.group?.deputyHeadma?.id == user.userId)
                    }
                    else -> false
                }
            }else false

            val visibleEditButton: Boolean = if(user.userRole != null){
                when(user.userRole){
                    UserRole.ADMIN -> true
                    UserRole.TEACHER -> {
                        type.row.teacher.id == (user.userId ?: 0)
                    }
                    UserRole.DEPUTY_HEADMAN, UserRole.HEADMAN -> {
                        !type.row.confirmation && (raportichka?.group?.headman?.id == user.userId ||
                                raportichka?.group?.deputyHeadma?.id == user.userId)
                    }
                    else -> false
                }
            }else false

            val visibleConfirmationEditButton = if(user.userRole != null){
                when(user.userRole){
                    UserRole.ADMIN -> true
                    UserRole.TEACHER -> user.userId == type.row.teacher.id
                    else -> false
                }
            }else false


            RaportichkaRowMenuUi(
                visibleDeleteButton = visibleDeleteButton,
                visibleEditButton = visibleEditButton,
                visibleConfirmationEditButton = visibleConfirmationEditButton,
                onClick = { menu ->
                    when(menu){
                        RaportichkaRowMenu.STUDENT_DETAILS -> onStudentDetailsScreen(type.row.student.id)
                        RaportichkaRowMenu.TEACHER_DETAILS -> onTeacherDetailsScreen(type.row.teacher.id)
                        RaportichkaRowMenu.SUBJECT_DETAILS -> onSubjectDetailsScreen(type.row.subject.id)
                        RaportichkaRowMenu.CONFIRMATION_EDIT -> onRaportichkaUpdateConfirmation(type.row.id)
                        RaportichkaRowMenu.EDIT -> raportichka?.let {
                            updateRow(
                                raportichka.id,
                                raportichka.group.id,
                                type.row.id,
                                type.row.teacher.id,
                                type.row.numberLesson.toString(),
                                type.row.hours.toString(),
                                type.row.student.id,
                                type.row.subject.id
                            )
                        }
                        RaportichkaRowMenu.DELETE -> deleteRow(type.row.id)
                    }
                }
            )
        }
        RaportichkaSheetType.AddRaportichkaMenu -> {

            val visibleAddRowButton: Boolean = if(user.userRole != null && raportichka != null){
                when(user.userRole){
                    UserRole.ADMIN -> true
                    UserRole.DEPUTY_HEADMAN, UserRole.HEADMAN -> {
                        raportichka.group.headman?.id == user.userId
                                || raportichka.group.deputyHeadma?.id == user.userId
                    }
                    else -> false
                }
            }else {
                false
            }

            AddRaportichkaMenuUi(
                visibleAddRowButton = visibleAddRowButton,
                onClick = { menu ->
                    when(menu){
                        AddRaportichkaMenu.ADD_RAPORTICHKA -> onClickAddRaportichkaMenu()
                        AddRaportichkaMenu.RAPORTICHKA_ADD_ROW ->
                            onRaportichkaAddRow(raportichka!!.id, raportichka.group.id)
                    }
                }
            )
        }
        RaportichkaSheetType.AddRaportichka -> AddRaportichka(
            getGroup = getGroup,
            addRaportichka = addRaportichka
        )
        null -> EmptyUi()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun RaportichkaList(
    paddingValues:PaddingValues,
    pagerState:PagerState,
    raportichkaList: LazyPagingItems<Raportichka>,
    searchByStudentId: List<Int>? = null,
    searchByGroupsId: List<Int>? = null,
    searchBySubjectsId: List<Int>? = null,
    searchByTeacherId: List<Int>? = null,
    onClickRow:(row: RaportichkaRow) -> Unit
) {
    VerticalPager(
        count = raportichkaList.itemCount,
        state = pagerState,
        userScrollEnabled = false,
        contentPadding = paddingValues
    ) { pageIndex ->
        Box(modifier = Modifier.fillMaxSize()) {
            val raportichka = raportichkaList[pageIndex]

            if(raportichka?.rows != null){
                RaportichkaTable(
                    rows = raportichka.rows,
                    searchByStudentId = searchByStudentId,
                    searchByGroupsId = searchByGroupsId,
                    searchBySubjectsId = searchBySubjectsId,
                    searchByTeacherId = searchByTeacherId,
                    onClickRow = onClickRow,
                )
            }else{
                EmptyUi()
            }
        }
    }
}

@Composable
private fun RaportichkaRowMenuUi(
    visibleDeleteButton: Boolean,
    visibleEditButton: Boolean,
    visibleConfirmationEditButton: Boolean,
    onClick: (RaportichkaRowMenu) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        RaportichkaRowMenu.values().forEach { menu ->

            val visible = when (menu) {
                RaportichkaRowMenu.DELETE -> visibleDeleteButton
                RaportichkaRowMenu.EDIT -> visibleEditButton
                RaportichkaRowMenu.CONFIRMATION_EDIT -> visibleConfirmationEditButton
                else -> true
            }

            RaportichkaRowMenuItem(
                visible = visible,
                menu = menu,
                onClick = { onClick(menu) }
            )
        }
    }
}

@Composable
private fun RaportichkaRowMenuItem(
    visible: Boolean,
    menu: RaportichkaRowMenu,
    onClick: () -> Unit
) {
    if(visible){
        DropdownMenuItem(onClick = onClick) {
            Icon(
                imageVector = menu.icon,
                contentDescription = null,
                tint = if(menu == RaportichkaRowMenu.DELETE)
                    PgkTheme.colors.errorColor
                else
                    PgkTheme.colors.primaryText
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = stringResource(id = menu.textId),
                color = if(menu == RaportichkaRowMenu.DELETE)
                    PgkTheme.colors.errorColor
                else
                    PgkTheme.colors.primaryText,
                style = PgkTheme.typography.caption,
                fontFamily = PgkTheme.fontFamily.fontFamily
            )
        }
    }
}

@Composable
private fun AddRaportichkaMenuUi(
    visibleAddRowButton: Boolean,
    onClick: (AddRaportichkaMenu) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        AddRaportichkaMenu.values().forEach { menu ->
            if(menu == AddRaportichkaMenu.RAPORTICHKA_ADD_ROW && visibleAddRowButton){
                AddRaportichkaMenuItem(menu = menu,onClick = { onClick(menu) })
            }else if(menu != AddRaportichkaMenu.RAPORTICHKA_ADD_ROW) {
                AddRaportichkaMenuItem(menu = menu,onClick = { onClick(menu) })
            }
        }
    }
}

@Composable
private fun AddRaportichkaMenuItem(
    menu: AddRaportichkaMenu,
    onClick: () -> Unit
) {
    DropdownMenuItem(onClick = onClick) {
        Icon(
            imageVector = menu.icon,
            contentDescription = null,
            tint = PgkTheme.colors.primaryText
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = stringResource(id = menu.textId),
            color = PgkTheme.colors.primaryText,
            style = PgkTheme.typography.caption,
            fontFamily = PgkTheme.fontFamily.fontFamily
        )
    }
}

@Composable
private fun AddRaportichka(
    getGroup: (search:String?) -> LazyPagingItems<Group>,
    addRaportichka: (groupId: Int) -> Unit
) {
    var groupsSearchText by remember { mutableStateOf("") }
    var groupIdSelected by remember { mutableStateOf<Int?>(null) }

    var groups by remember { mutableStateOf<LazyPagingItems<Group>?>(null) }

    LaunchedEffect(key1 = groupsSearchText, block = {
        groups = getGroup.invoke(groupsSearchText)
    })

    Column {

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(visible = groups != null) {
            SortingItem(
                title = stringResource(id = R.string.group_choose),
                content = groups!!,
                searchText = groupsSearchText,
                selectedItem = { group ->
                    group.id == groupIdSelected
                },
                onClickItem = { group ->
                    groupIdSelected = group.id
                },
                onSearchTextChange = {
                    groupsSearchText = it
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(visible = groupIdSelected != null) {
            TextButton(onClick = { groupIdSelected?.let { addRaportichka(it) } }) {
                Text(
                    text = stringResource(id = R.string.add),
                    color = PgkTheme.colors.tintColor,
                    style = PgkTheme.typography.body,
                    fontFamily = PgkTheme.fontFamily.fontFamily,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}