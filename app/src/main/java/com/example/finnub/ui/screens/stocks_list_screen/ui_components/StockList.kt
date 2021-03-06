package com.example.finnub.ui.screens.stocks_list_screen.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import com.example.finnub.data.api.models.SimpleStock
import com.example.finnub.ui.screens.stocks_list_screen.PageSwitcher
import com.example.finnub.ui.screens.stocks_list_screen.StocksListViewModel
import com.example.finnub.ui.theme.finnhubGreen
import com.example.finnub.utils.LoadingState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun StocksList(vm: StocksListViewModel) {


    var stockList:List<SimpleStock> by remember {
        mutableStateOf(listOf())
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    vm.pageStocksList.observe(lifecycleOwner){simpleStockList ->
        stockList = simpleStockList
    }

    val state = rememberLazyListState()

    val loadingState by vm.stocksListLoadingState.collectAsState()

    val refreshState = rememberSwipeRefreshState(isRefreshing = loadingState == LoadingState.LoadingInProcess)

    Box(
        Modifier
        .fillMaxSize()
    ) {
        SwipeRefresh(
            state = refreshState,
            refreshTriggerDistance = 160.dp,
            onRefresh = {vm.Refresh()},
            indicator = { state, refreshDist ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshDist,
                    contentColor = finnhubGreen
                )
            }
        ) {
            LazyColumn(state = state, modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
            ){
                if (loadingState == LoadingState.LoadingInProcess)
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(700.dp)) {
                            CircularProgressIndicator(
                                color = finnhubGreen,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                else
                    itemsIndexed(stockList){ index: Int, simpleStock: SimpleStock ->
                        StockListItem(
                            simpleStock = simpleStock,
                            vm = vm,
                            index = index)
                    }
            }
        }


        PageSwitcher(
            state = state,
            vm = vm,
            modifier = Modifier
                .height(80.dp)
                .align(Alignment.BottomCenter)
        )

    }
}