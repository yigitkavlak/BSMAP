package com.yigitkavlak.mapduty

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.fragment_task.*


class TaskFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        val saveButton = view.findViewById<View>(R.id.saveButton) as Button
        val cancelButton = view.findViewById<View>(R.id.cancelButton) as Button

        saveButton.setOnClickListener {
            Toast.makeText(context!!, " Save Button Clicked", Toast.LENGTH_LONG).show()
            (activity as MapsActivity).closeTaskFragment()
            (activity as MapsActivity).setButtonVisible()
            (activity as MapsActivity).clearMap()
        }

        cancelButton.setOnClickListener {

            // AlertDialog nesnemizi üretiyoruz
            val alert = AlertDialog.Builder(context)

            // Başlık
            alert.setTitle("İptal")

            //Mesaj
            alert.setMessage("İptal etmek istediğinize emin misiniz?")

            //Herhangi bir boşluğa basınca kapanmaması için true olursa kapanır
            //Geri tuşununu da pasif hale getiriyoruz
            alert.setCancelable(false);


            alert.setPositiveButton("Evet") { dialogInterface: DialogInterface, i: Int ->

                // Evet butonuna tıklayınca olacaklar
                Toast.makeText(context, "Kayıt İptal Edildi", Toast.LENGTH_LONG).show()

                (activity as MapsActivity).closeTaskFragment()
                (activity as MapsActivity).setButtonVisible()
                (activity as MapsActivity).clearMap()
                taskDetailText.text.clear()
                taskNameText.text.clear()
            }

            alert.setNegativeButton("Hayır") { dialogInterface: DialogInterface, i: Int ->

                // Hayır butonuna tıklayınca olacaklar
                  Toast.makeText(context,"İşleme Devam Edebilirsiniz",Toast.LENGTH_LONG).show()


            }

            alert.show()


        }



        return view
    }


}