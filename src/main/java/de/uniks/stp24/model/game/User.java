package de.uniks.stp24.model.game;
import java.util.Objects;
import java.time.LocalDateTime;
import java.beans.PropertyChangeSupport;

public class User
{
   public static final String PROPERTY__ID = "_id";
   public static final String PROPERTY_CREATED_AT = "createdAt";
   public static final String PROPERTY_UPDATED_AT = "updatedAt";
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_READY = "ready";
   public static final String PROPERTY_EMPIRE = "empire";
   public static final String PROPERTY_GAME = "game";
   private String _id;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
   protected PropertyChangeSupport listeners;
   private String name;
   private boolean ready;
   private Empire empire;
   private Game game;

   public String get_id()
   {
      return this._id;
   }

   public User set_id(String value)
   {
      if (Objects.equals(value, this._id))
      {
         return this;
      }

      final String oldValue = this._id;
      this._id = value;
      this.firePropertyChange(PROPERTY__ID, oldValue, value);
      return this;
   }

   public LocalDateTime getCreatedAt()
   {
      return this.createdAt;
   }

   public User setCreatedAt(LocalDateTime value)
   {
      if (Objects.equals(value, this.createdAt))
      {
         return this;
      }

      final LocalDateTime oldValue = this.createdAt;
      this.createdAt = value;
      this.firePropertyChange(PROPERTY_CREATED_AT, oldValue, value);
      return this;
   }

   public LocalDateTime getUpdatedAt()
   {
      return this.updatedAt;
   }

   public User setUpdatedAt(LocalDateTime value)
   {
      if (Objects.equals(value, this.updatedAt))
      {
         return this;
      }

      final LocalDateTime oldValue = this.updatedAt;
      this.updatedAt = value;
      this.firePropertyChange(PROPERTY_UPDATED_AT, oldValue, value);
      return this;
   }

   public String getName()
   {
      return this.name;
   }

   public User setName(String value)
   {
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_NAME, oldValue, value);
      return this;
   }

   public boolean isReady()
   {
      return this.ready;
   }

   public User setReady(boolean value)
   {
      if (value == this.ready)
      {
         return this;
      }

      final boolean oldValue = this.ready;
      this.ready = value;
      this.firePropertyChange(PROPERTY_READY, oldValue, value);
      return this;
   }

   public Empire getEmpire()
   {
      return this.empire;
   }

   public User setEmpire(Empire value)
   {
      if (this.empire == value)
      {
         return this;
      }

      final Empire oldValue = this.empire;
      if (this.empire != null)
      {
         this.empire = null;
         oldValue.setOwner(null);
      }
      this.empire = value;
      if (value != null)
      {
         value.setOwner(this);
      }
      this.firePropertyChange(PROPERTY_EMPIRE, oldValue, value);
      return this;
   }

   public Game getGame()
   {
      return this.game;
   }

   public User setGame(Game value)
   {
      if (this.game == value)
      {
         return this;
      }

      final Game oldValue = this.game;
      if (this.game != null)
      {
         this.game = null;
         oldValue.withoutMembers(this);
      }
      this.game = value;
      if (value != null)
      {
         value.withMembers(this);
      }
      this.firePropertyChange(PROPERTY_GAME, oldValue, value);
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.get_id());
      result.append(' ').append(this.getName());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setEmpire(null);
      this.setGame(null);
   }
}
