package com.example.telega.telega.UI.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.*
import com.example.telega.telega.activities.RegisterActivity
import com.example.telega.telega.utilits.*
import com.example.telegram.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override fun onResume()
    {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        settings_bio.text = USER.bio
        setting_fullName.text = USER.fullname
        settings_phone_number.text = USER.phone
        settings_status.text = USER.state
        settings_userName.text = USER.username
        settings_btn_change_userName.setOnClickListener { replaceFragment(ChangeUsernameFragment()) }
        settings_btn_change_bio.setOnClickListener { replaceFragment(ChangeBioFragment()) }
        settings_change_photo.setOnClickListener { changePhotoUser() }
        settings_user_photo.downloadAndSetImage(USER.photoUrl)
    }

    private fun changePhotoUser() {
        /* Изменения фото пользователя */
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY,this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        /* Создания выпадающего меню*/
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /* Слушатель выбора пунктов выпадающего меню */
        when (item.itemId) {
            R.id.settigs_btn_action_menu_exit -> {
                AUTH.signOut()
                APP_ACTIVITY.replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /* Активность которая запускается для получения картинки для фото пользователя */
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null)
        {

            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(REF_PROFILE_IMAGE)
                .child(CURRENT_UID)
            putImageToStorage(uri,path){
                getUrlFromStorage(path){
                    putUrlToDataBase(it){
                        settings_user_photo.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_update))
                        USER.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }
        }
    }
}