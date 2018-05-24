package guepardoapps.lib.openweather.utils

import android.content.Context
import android.support.annotation.NonNull
import android.util.Log
import guepardoapps.lib.openweather.database.logging.DbHandler
import guepardoapps.lib.openweather.database.logging.DbLog
import guepardoapps.lib.openweather.database.logging.Severity
import java.sql.Date
import java.util.*

class Logger private constructor() {
    private var _loggingEnabled: Boolean = true
    private var _writeToDatabaseEnabled: Boolean = true

    private var _dbHandler: DbHandler? = null

    init {
    }

    private object Holder {
        val instance: Logger = Logger()
    }

    companion object {
        val instance: Logger by lazy { Holder.instance }
    }

    fun initialize(context: Context) {
        if (_dbHandler != null) {
            return
        }
        _dbHandler = DbHandler(context, null)
    }

    fun setLoggingEnabled(loggingEnabled: Boolean) {
        _loggingEnabled = loggingEnabled
    }

    fun isLoggingEnabled(): Boolean {
        return _loggingEnabled
    }

    fun setWriteToDatabaseEnabled(writeToDatabaseEnabled: Boolean) {
        _writeToDatabaseEnabled = writeToDatabaseEnabled
    }

    fun isWriteToDatabaseEnabled(): Boolean {
        return _writeToDatabaseEnabled
    }

    fun <T> verbose(@NonNull tag: String, @NonNull description: T) {
        if (_loggingEnabled) {
            Log.v(tag, description.toString())

            if (_writeToDatabaseEnabled && _dbHandler != null) {
                _dbHandler?.addLog(
                        DbLog(-1,
                                Date(Calendar.getInstance().timeInMillis),
                                Severity.Verbose,
                                tag,
                                description.toString()))
            }
        }
    }

    fun <T> debug(@NonNull tag: String, @NonNull description: T) {
        if (_loggingEnabled) {
            Log.d(tag, description.toString())

            if (_writeToDatabaseEnabled && _dbHandler != null) {
                _dbHandler?.addLog(
                        DbLog(-1,
                                Date(Calendar.getInstance().timeInMillis),
                                Severity.Debug,
                                tag,
                                description.toString()))
            }
        }
    }

    fun <T> info(@NonNull tag: String, @NonNull description: T) {
        if (_loggingEnabled) {
            Log.i(tag, description.toString())

            if (_writeToDatabaseEnabled && _dbHandler != null) {
                _dbHandler?.addLog(
                        DbLog(-1,
                                Date(Calendar.getInstance().timeInMillis),
                                Severity.Info,
                                tag,
                                description.toString()))
            }
        }
    }

    fun <T> warning(@NonNull tag: String, @NonNull description: T) {
        if (_loggingEnabled) {
            Log.w(tag, description.toString())

            if (_writeToDatabaseEnabled && _dbHandler != null) {
                _dbHandler?.addLog(
                        DbLog(-1,
                                Date(Calendar.getInstance().timeInMillis),
                                Severity.Warning,
                                tag,
                                description.toString()))
            }
        }
    }

    fun <T> error(@NonNull tag: String, @NonNull description: T) {
        if (_loggingEnabled) {
            Log.e(tag, description.toString())

            if (_writeToDatabaseEnabled && _dbHandler != null) {
                _dbHandler?.addLog(
                        DbLog(-1,
                                Date(Calendar.getInstance().timeInMillis),
                                Severity.Error,
                                tag,
                                description.toString()))
            }
        }
    }
}