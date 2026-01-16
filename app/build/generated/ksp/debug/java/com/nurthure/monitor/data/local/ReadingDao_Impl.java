package com.nurthure.monitor.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReadingDao_Impl implements ReadingDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReadingEntity> __insertionAdapterOfReadingEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldReadings;

  public ReadingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReadingEntity = new EntityInsertionAdapter<ReadingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `readings` (`id`,`timestamp`,`respirationValue`,`bodyTempValue`,`co2Value`,`vocValue`,`postureState`,`audioState`,`radarActive`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReadingEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        if (entity.getRespirationValue() == null) {
          statement.bindNull(3);
        } else {
          statement.bindDouble(3, entity.getRespirationValue());
        }
        if (entity.getBodyTempValue() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getBodyTempValue());
        }
        if (entity.getCo2Value() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getCo2Value());
        }
        if (entity.getVocValue() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getVocValue());
        }
        if (entity.getPostureState() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPostureState());
        }
        if (entity.getAudioState() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getAudioState());
        }
        final int _tmp = entity.getRadarActive() ? 1 : 0;
        statement.bindLong(9, _tmp);
      }
    };
    this.__preparedStmtOfDeleteOldReadings = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM readings WHERE timestamp < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ReadingEntity reading, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfReadingEntity.insert(reading);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldReadings(final long before, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldReadings.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, before);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOldReadings.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getReadingsLastHours(final long since,
      final Continuation<? super List<ReadingEntity>> $completion) {
    final String _sql = "SELECT * FROM readings WHERE timestamp >= ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReadingEntity>>() {
      @Override
      @NonNull
      public List<ReadingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfRespirationValue = CursorUtil.getColumnIndexOrThrow(_cursor, "respirationValue");
          final int _cursorIndexOfBodyTempValue = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyTempValue");
          final int _cursorIndexOfCo2Value = CursorUtil.getColumnIndexOrThrow(_cursor, "co2Value");
          final int _cursorIndexOfVocValue = CursorUtil.getColumnIndexOrThrow(_cursor, "vocValue");
          final int _cursorIndexOfPostureState = CursorUtil.getColumnIndexOrThrow(_cursor, "postureState");
          final int _cursorIndexOfAudioState = CursorUtil.getColumnIndexOrThrow(_cursor, "audioState");
          final int _cursorIndexOfRadarActive = CursorUtil.getColumnIndexOrThrow(_cursor, "radarActive");
          final List<ReadingEntity> _result = new ArrayList<ReadingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReadingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final Float _tmpRespirationValue;
            if (_cursor.isNull(_cursorIndexOfRespirationValue)) {
              _tmpRespirationValue = null;
            } else {
              _tmpRespirationValue = _cursor.getFloat(_cursorIndexOfRespirationValue);
            }
            final Float _tmpBodyTempValue;
            if (_cursor.isNull(_cursorIndexOfBodyTempValue)) {
              _tmpBodyTempValue = null;
            } else {
              _tmpBodyTempValue = _cursor.getFloat(_cursorIndexOfBodyTempValue);
            }
            final Integer _tmpCo2Value;
            if (_cursor.isNull(_cursorIndexOfCo2Value)) {
              _tmpCo2Value = null;
            } else {
              _tmpCo2Value = _cursor.getInt(_cursorIndexOfCo2Value);
            }
            final Float _tmpVocValue;
            if (_cursor.isNull(_cursorIndexOfVocValue)) {
              _tmpVocValue = null;
            } else {
              _tmpVocValue = _cursor.getFloat(_cursorIndexOfVocValue);
            }
            final String _tmpPostureState;
            if (_cursor.isNull(_cursorIndexOfPostureState)) {
              _tmpPostureState = null;
            } else {
              _tmpPostureState = _cursor.getString(_cursorIndexOfPostureState);
            }
            final String _tmpAudioState;
            if (_cursor.isNull(_cursorIndexOfAudioState)) {
              _tmpAudioState = null;
            } else {
              _tmpAudioState = _cursor.getString(_cursorIndexOfAudioState);
            }
            final boolean _tmpRadarActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRadarActive);
            _tmpRadarActive = _tmp != 0;
            _item = new ReadingEntity(_tmpId,_tmpTimestamp,_tmpRespirationValue,_tmpBodyTempValue,_tmpCo2Value,_tmpVocValue,_tmpPostureState,_tmpAudioState,_tmpRadarActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllReadings(final Continuation<? super List<ReadingEntity>> $completion) {
    final String _sql = "SELECT * FROM readings ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ReadingEntity>>() {
      @Override
      @NonNull
      public List<ReadingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfRespirationValue = CursorUtil.getColumnIndexOrThrow(_cursor, "respirationValue");
          final int _cursorIndexOfBodyTempValue = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyTempValue");
          final int _cursorIndexOfCo2Value = CursorUtil.getColumnIndexOrThrow(_cursor, "co2Value");
          final int _cursorIndexOfVocValue = CursorUtil.getColumnIndexOrThrow(_cursor, "vocValue");
          final int _cursorIndexOfPostureState = CursorUtil.getColumnIndexOrThrow(_cursor, "postureState");
          final int _cursorIndexOfAudioState = CursorUtil.getColumnIndexOrThrow(_cursor, "audioState");
          final int _cursorIndexOfRadarActive = CursorUtil.getColumnIndexOrThrow(_cursor, "radarActive");
          final List<ReadingEntity> _result = new ArrayList<ReadingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReadingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final Float _tmpRespirationValue;
            if (_cursor.isNull(_cursorIndexOfRespirationValue)) {
              _tmpRespirationValue = null;
            } else {
              _tmpRespirationValue = _cursor.getFloat(_cursorIndexOfRespirationValue);
            }
            final Float _tmpBodyTempValue;
            if (_cursor.isNull(_cursorIndexOfBodyTempValue)) {
              _tmpBodyTempValue = null;
            } else {
              _tmpBodyTempValue = _cursor.getFloat(_cursorIndexOfBodyTempValue);
            }
            final Integer _tmpCo2Value;
            if (_cursor.isNull(_cursorIndexOfCo2Value)) {
              _tmpCo2Value = null;
            } else {
              _tmpCo2Value = _cursor.getInt(_cursorIndexOfCo2Value);
            }
            final Float _tmpVocValue;
            if (_cursor.isNull(_cursorIndexOfVocValue)) {
              _tmpVocValue = null;
            } else {
              _tmpVocValue = _cursor.getFloat(_cursorIndexOfVocValue);
            }
            final String _tmpPostureState;
            if (_cursor.isNull(_cursorIndexOfPostureState)) {
              _tmpPostureState = null;
            } else {
              _tmpPostureState = _cursor.getString(_cursorIndexOfPostureState);
            }
            final String _tmpAudioState;
            if (_cursor.isNull(_cursorIndexOfAudioState)) {
              _tmpAudioState = null;
            } else {
              _tmpAudioState = _cursor.getString(_cursorIndexOfAudioState);
            }
            final boolean _tmpRadarActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRadarActive);
            _tmpRadarActive = _tmp != 0;
            _item = new ReadingEntity(_tmpId,_tmpTimestamp,_tmpRespirationValue,_tmpBodyTempValue,_tmpCo2Value,_tmpVocValue,_tmpPostureState,_tmpAudioState,_tmpRadarActive);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLatestReading(final Continuation<? super ReadingEntity> $completion) {
    final String _sql = "SELECT * FROM readings ORDER BY timestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReadingEntity>() {
      @Override
      @Nullable
      public ReadingEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfRespirationValue = CursorUtil.getColumnIndexOrThrow(_cursor, "respirationValue");
          final int _cursorIndexOfBodyTempValue = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyTempValue");
          final int _cursorIndexOfCo2Value = CursorUtil.getColumnIndexOrThrow(_cursor, "co2Value");
          final int _cursorIndexOfVocValue = CursorUtil.getColumnIndexOrThrow(_cursor, "vocValue");
          final int _cursorIndexOfPostureState = CursorUtil.getColumnIndexOrThrow(_cursor, "postureState");
          final int _cursorIndexOfAudioState = CursorUtil.getColumnIndexOrThrow(_cursor, "audioState");
          final int _cursorIndexOfRadarActive = CursorUtil.getColumnIndexOrThrow(_cursor, "radarActive");
          final ReadingEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final Float _tmpRespirationValue;
            if (_cursor.isNull(_cursorIndexOfRespirationValue)) {
              _tmpRespirationValue = null;
            } else {
              _tmpRespirationValue = _cursor.getFloat(_cursorIndexOfRespirationValue);
            }
            final Float _tmpBodyTempValue;
            if (_cursor.isNull(_cursorIndexOfBodyTempValue)) {
              _tmpBodyTempValue = null;
            } else {
              _tmpBodyTempValue = _cursor.getFloat(_cursorIndexOfBodyTempValue);
            }
            final Integer _tmpCo2Value;
            if (_cursor.isNull(_cursorIndexOfCo2Value)) {
              _tmpCo2Value = null;
            } else {
              _tmpCo2Value = _cursor.getInt(_cursorIndexOfCo2Value);
            }
            final Float _tmpVocValue;
            if (_cursor.isNull(_cursorIndexOfVocValue)) {
              _tmpVocValue = null;
            } else {
              _tmpVocValue = _cursor.getFloat(_cursorIndexOfVocValue);
            }
            final String _tmpPostureState;
            if (_cursor.isNull(_cursorIndexOfPostureState)) {
              _tmpPostureState = null;
            } else {
              _tmpPostureState = _cursor.getString(_cursorIndexOfPostureState);
            }
            final String _tmpAudioState;
            if (_cursor.isNull(_cursorIndexOfAudioState)) {
              _tmpAudioState = null;
            } else {
              _tmpAudioState = _cursor.getString(_cursorIndexOfAudioState);
            }
            final boolean _tmpRadarActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfRadarActive);
            _tmpRadarActive = _tmp != 0;
            _result = new ReadingEntity(_tmpId,_tmpTimestamp,_tmpRespirationValue,_tmpBodyTempValue,_tmpCo2Value,_tmpVocValue,_tmpPostureState,_tmpAudioState,_tmpRadarActive);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
