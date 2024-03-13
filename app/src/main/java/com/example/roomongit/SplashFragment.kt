package com.example.roomongit

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton

class SplashFragment : Fragment() {
    private val animatorSet = AnimatorSet()
    private var signInButton: SignInButton? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_login_screen, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInButton = view.findViewById(R.id.signInButton)
        val image: ImageView = view.findViewById(R.id.image)
        startAnimation(image)
        val googleSignInInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("878710138401-i9fupgge3m5un1fv5d5tk43dthlp5tbo.apps.googleusercontent.com")
                .requestEmail()
                .build()
        val googleSignInClient =
            GoogleSignIn.getClient(requireContext(), googleSignInInOptions)
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        val activity = requireActivity() as OnAuthLaunch
        if(account == null) {
            showSignInButton()
        } else {
            animatorSet.cancel()
            activity.showListFragment()
        }
        signInButton?.setOnClickListener {
            activity.launch(googleSignInClient.signInIntent)
        }
    }
    private fun showSignInButton(){
        signInButton?.visibility = View.VISIBLE
        animatorSet.start()
    }
    private fun startAnimation(image: ImageView) {
        val scaleXAnimation =  ObjectAnimator.ofFloat(image, View.SCALE_X, 0.5f, 1f)
        scaleXAnimation.repeatMode = ObjectAnimator.REVERSE
        scaleXAnimation.repeatCount = ObjectAnimator.INFINITE
        val scaleYAnimation =  ObjectAnimator.ofFloat(image, View.SCALE_Y, 0.5f, 1f)
        scaleYAnimation.repeatMode = ObjectAnimator.REVERSE
        scaleYAnimation.repeatCount = ObjectAnimator.INFINITE
        animatorSet.playTogether(scaleXAnimation, scaleYAnimation)
        animatorSet.duration = 1000
    }
}