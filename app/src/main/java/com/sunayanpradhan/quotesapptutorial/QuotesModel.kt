package com.sunayanpradhan.quotesapptutorial

data class QuotesModel(var quotesText:String,var quotesAuthor: String)
{
    constructor():this(
        "",
        ""
    )


}