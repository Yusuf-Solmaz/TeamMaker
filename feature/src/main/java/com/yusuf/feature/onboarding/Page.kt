import android.content.Context
import com.yusuf.feature.R

data class Page(
    val title: String,
    val description: String,
    val lottieFile: Int
)

fun getOnboardingPages(context: Context): List<Page> {
    return listOf(
        Page(
            title = context.getString(R.string.onboarding_title_1),
            description = context.getString(R.string.onboarding_description_1),
            lottieFile = R.raw.onboarding_anim_1
        ),
        Page(
            title = context.getString(R.string.onboarding_title_2),
            description = context.getString(R.string.onboarding_description_2),
            lottieFile = R.raw.onboarding_anim_2
        ),
        Page(
            title = context.getString(R.string.onboarding_title_3),
            description = context.getString(R.string.onboarding_description_3),
            lottieFile = R.raw.onboarding_anim_3
        )
    )
}