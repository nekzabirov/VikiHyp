package com.vikihyp.shared.tool

import androidx.compose.runtime.Composable
import com.nekzabirov.firebaseapp.KActivity
import com.nekzabirov.firebaseapp.KContext

@Composable
expect fun getActivity(): KActivity

expect fun getKContext(): KContext