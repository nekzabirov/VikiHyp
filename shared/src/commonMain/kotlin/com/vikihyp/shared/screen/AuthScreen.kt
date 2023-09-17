package com.vikihyp.shared.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vikihyp.shared.tool.stringResource
import com.vikihyp.shared.ui.GoogleSignIn
import com.vikihyp.shared.ui.VikiButton
import com.vikihyp.shared.ui.VikiButtonSecondary
import com.vikihyp.shared.ui.viewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

private fun DrawScope.drawWave(color: Color, size: Size, isTop: Boolean) {
    fun wavePath(): Path {
        val path = Path()

        val width = size.width
        var height = size.height

        if (isTop) {
            height = size.height * 1.5f

            path.moveTo(0f, 0f)
            path.lineTo(0f, height)
            path.quadraticBezierTo(x1 = 0f, y1 = height / 1.5f, x2 = width / 2, y2 = height / 1.5f)
            path.lineTo(width / 2 + 200f, height / 1.5f)
            path.quadraticBezierTo(x1 = width, y1 = height / 1.5f, x2 = width, y2 = 0f)
            path.lineTo(width, 0f)
            path.lineTo(0f, 0f)
        } else {
            val yScreen = height
            height = 250f
            val top = yScreen - height

            path.moveTo(0f, yScreen)
            path.quadraticBezierTo(x1 = 0f, y1 = top, x2 = width / 2, y2 = top)
            path.lineTo(width / 2 + 200f, top)
            path.quadraticBezierTo(x1 = width, y1 = top, x2 = width, top - height)
            path.lineTo(width, yScreen)
            path.lineTo(0f, yScreen)
        }

        return path
    }

    val shadowOffset = if (isTop) 20f else -20f
    val shadowColor = color.copy(alpha = 0.3f)
    val shadowPath = wavePath().apply {
        translate(Offset(2f, shadowOffset))
    }

    val path = wavePath()

    drawPath(
        path = shadowPath,
        brush = Brush.verticalGradient(
            colors = listOf(color, shadowColor),
            startY = if (isTop) 0f else shadowOffset,
            endY = if (isTop) shadowOffset else 0f
        )
    )
    drawPath(path, color = color, style = Fill)
}

@Composable
internal fun AuthScreen(mViewModel: AuthViewModel = viewModel { AuthViewModel() }) {
    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind { drawWave(color = primaryColor, size = size, isTop = false) },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Title()

        JoinContent(mViewModel)

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                onClick = {}
            ) {
                Text(
                    stringResource("auth_screen_private_policy_label"),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Title() {
    val primaryColor = MaterialTheme.colorScheme.primary

    Column(modifier = Modifier
        .drawBehind { drawWave(color = primaryColor, size = size, isTop = true) }) {
        TopAppBar(
            title = {
                Text(
                    modifier = Modifier,
                    text = stringResource("auth_screen_title")
                )
            },
            navigationIcon = {},
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
            text = stringResource("auth_screen_label"),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun JoinContent(mViewModel: AuthViewModel) = Column(
    modifier = Modifier.padding(horizontal = 16.dp).offset(y = 32.dp),
    verticalArrangement = Arrangement.spacedBy(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = stringResource("auth_screen_join_label"),
        style = MaterialTheme.typography.titleMedium
    )

    JoinPhone(mViewModel)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(
            modifier = Modifier.weight(1f).height(1.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Text(
            text = stringResource("auth_screen_join_label_or_method_label").lowercase(),
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(
            modifier = Modifier.weight(1f).height(1.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }

    JoinSocialButtons(mViewModel)
}

@Composable
private fun JoinPhone(mViewModel: AuthViewModel) = Column(
    modifier = Modifier.animateContentSize(animationSpec = tween(easing = LinearOutSlowInEasing)),
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    val state by remember { mViewModel.state }.collectAsState()

    var phoneNumber by remember { mutableStateOf("") }
    var smsCode by remember { mutableStateOf("") }

    val verificationID = remember(state) {
        (state as? AuthState.PhoneVerification)?.verificationID ?: ""
    }
    var isVerificationMode by remember { mutableStateOf(false) }

    val isPhoneFail = remember(state) {
        state is AuthState.PhoneVerification
                && (state as AuthState.PhoneVerification).isFail
                && (state as AuthState.PhoneVerification).verificationID.isEmpty()
    }
    val isCodeFail = remember(state) {
        state is AuthState.PhoneVerification
                && (state as AuthState.PhoneVerification).isFail
                && (state as AuthState.PhoneVerification).verificationID.isNotEmpty()
    }
    val isLoading = remember(state) {
        state is AuthState.PhoneVerification
                && (state as AuthState.PhoneVerification).isLoading
    }

    var resendCodeTimeOut by remember { mutableStateOf(0) }

    OutlinedTextField(
        value = phoneNumber,
        singleLine = true,
        enabled = !isVerificationMode,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        onValueChange = { phoneNumber = it },
        placeholder = { Text(stringResource("auth_screen_join_with_phone_phonenumber_field_placeholder")) },
        label = { Text(stringResource("auth_screen_join_with_phone_phonenumber_field_label")) },
        isError = isPhoneFail,
        modifier = Modifier.fillMaxWidth()
    )

    if (isVerificationMode) {
        OutlinedTextField(
            value = smsCode,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            onValueChange = { smsCode = it },
            placeholder = { Text(stringResource("auth_screen_join_with_phone_smscode_field_placeholder")) },
            label = { Text(stringResource("auth_screen_join_with_phone_smscode_field_label")) },
            isError = isCodeFail,
            supportingText = if (isCodeFail) {
                { Text(stringResource("auth_screen_join_with_phone_smscode_verify_fail_error")) }
            } else {
                null
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        if (isVerificationMode)
            VikiButtonSecondary(
                modifier = Modifier.weight(1f),
                label = "Back",
            ) { isVerificationMode = false }

        VikiButton(
            modifier = Modifier.weight(1f),
            label = if (isVerificationMode)
                stringResource("auth_screen_join_with_phone_phonenumber_verify_button_next_label")
            else if (resendCodeTimeOut == 0)
                stringResource("auth_screen_join_with_phone_phonenumber_button_next_label")
            else
                stringResource(
                    "auth_screen_join_with_phone_phonenumber_button_next_resend_label",
                    resendCodeTimeOut
                ),
            enable = (phoneNumber.isNotBlank() && !isVerificationMode && resendCodeTimeOut == 0) || (isVerificationMode && smsCode.length == 6),
            loading = isLoading
        ) {
            if (isVerificationMode)
                mViewModel.sendEvent(
                    AuthViewEvent.PhoneSignIn(
                        verificationID = verificationID,
                        verificationCode = smsCode
                    )
                )
            else
                mViewModel.sendEvent(AuthViewEvent.SendPhoneVerification(phoneNumber))
        }
    }

    LaunchedEffect(mViewModel) {
        var resendCodeTimeOutJob: Job? = null

        mViewModel.state
            .onEach {
                if (it is AuthState.PhoneVerification && !it.isLoading) {
                    resendCodeTimeOutJob?.cancel()
                    resendCodeTimeOutJob = launch {
                        resendCodeTimeOut = 60
                        while (resendCodeTimeOut != 0) {
                            resendCodeTimeOut--
                            delay(1000)
                        }
                    }
                    isVerificationMode = true
                }
            }
            .launchIn(this)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun JoinSocialButtons(mViewModel: AuthViewModel) {
    val state by remember { mViewModel.state }.collectAsState()

    val googleSignIn = remember { GoogleSignIn() }

    googleSignIn.register { tokenID, _ -> mViewModel.sendEvent(AuthViewEvent.GoogleSignIn(tokenID)) }

    Row {
        VikiButton(
            icon = painterResource("drawable/icon_google.xml"),
            loading = (state as? AuthState.GoogleSignIn)?.isLoading ?: false
        ) { googleSignIn.request() }

        Spacer(modifier = Modifier.width(8.dp))
        VikiButton(icon = painterResource("drawable/icon_fb.xml")) {}
        Spacer(modifier = Modifier.width(8.dp))

        VikiButton(icon = painterResource("drawable/icon_apple.xml")) {}
    }
}