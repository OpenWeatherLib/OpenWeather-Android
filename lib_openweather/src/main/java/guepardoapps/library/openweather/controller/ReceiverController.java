package guepardoapps.library.openweather.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import guepardoapps.library.openweather.common.utils.Logger;

public class ReceiverController implements Serializable {

    private static final long serialVersionUID = 2288241732336744506L;

    private static String TAG = ReceiverController.class.getSimpleName();

    private Logger _logger;
    private Context _context;
    private List<BroadcastReceiver> _registeredReceiver;

    public ReceiverController(@NonNull Context context) {
        _logger = new Logger(TAG);
        _context = context;
        _registeredReceiver = new ArrayList<>();
    }

    public void RegisterReceiver(
            @NonNull BroadcastReceiver receiver,
            @NonNull String[] actions) {
        _logger.Debug("Registering new receiver! " + receiver.toString());

        IntentFilter downloadStateFilter = new IntentFilter();
        for (String action : actions) {
            downloadStateFilter.addAction(action);
        }

        unregisterReceiver(receiver);

        _context.registerReceiver(receiver, downloadStateFilter);
        _registeredReceiver.add(receiver);
    }

    private void unregisterReceiver(@NonNull BroadcastReceiver receiver) {
        _logger.Debug("Trying to unregister receiver " + receiver.toString());

        for (int index = 0; index < _registeredReceiver.size(); index++) {
            if (_registeredReceiver.get(index) == receiver) {
                try {
                    _context.unregisterReceiver(receiver);
                    _registeredReceiver.remove(index);
                } catch (Exception e) {
                    _logger.Error(e.toString());
                }
                break;
            }
        }
    }

    public void Dispose() {
        _logger.Debug("Dispose");

        for (int index = 0; index < _registeredReceiver.size(); index++) {
            try {
                _context.unregisterReceiver(_registeredReceiver.get(index));
                _registeredReceiver.remove(index);
            } catch (Exception e) {
                _logger.Error(e.toString());
            }
        }
    }
}