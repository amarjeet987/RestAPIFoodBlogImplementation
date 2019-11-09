package rivy.assignment.foodblogapp.REST

import io.reactivex.Observable
import retrofit2.http.GET
import rivy.assignment.foodblogapp.models.ParentModel

interface API {
    @GET(".") fun fetchBlogs() : Observable<ParentModel>
}