package de.uniks.stp24.model.game;
import java.time.LocalDateTime;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Ship
{
   public static final String PROPERTY_CREATED_AT = "createdAt";
   public static final String PROPERTY_UPDATED_AT = "updatedAt";
   public static final String PROPERTY__ID = "_id";
   public static final String PROPERTY_TYPE = "type";
   public static final String PROPERTY_EMPIRE = "empire";
   public static final String PROPERTY_FLEET = "fleet";
   public static final String PROPERTY_HEALTH = "health";
   public static final String PROPERTY_EXPERIENCE = "experience";
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
   private String _id;
   private String type;
   private Empire empire;
   private Fleet fleet;
   protected PropertyChangeSupport listeners;
   private Integer health;
   private Integer experience;

   public LocalDateTime getCreatedAt()
   {
      return this.createdAt;
   }

   public Ship setCreatedAt(LocalDateTime value)
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

   public Ship setUpdatedAt(LocalDateTime value)
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

   public String get_id()
   {
      return this._id;
   }

   public Ship set_id(String value)
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

   public String getType()
   {
      return this.type;
   }

   public Ship setType(String value)
   {
      if (Objects.equals(value, this.type))
      {
         return this;
      }

      final String oldValue = this.type;
      this.type = value;
      this.firePropertyChange(PROPERTY_TYPE, oldValue, value);
      return this;
   }

   public Empire getEmpire()
   {
      return this.empire;
   }

   public Ship setEmpire(Empire value)
   {
      if (this.empire == value)
      {
         return this;
      }

      final Empire oldValue = this.empire;
      if (this.empire != null)
      {
         this.empire = null;
         oldValue.withoutShips(this);
      }
      this.empire = value;
      if (value != null)
      {
         value.withShips(this);
      }
      this.firePropertyChange(PROPERTY_EMPIRE, oldValue, value);
      return this;
   }

   public Fleet getFleet()
   {
      return this.fleet;
   }

   public Ship setFleet(Fleet value)
   {
      if (this.fleet == value)
      {
         return this;
      }

      final Fleet oldValue = this.fleet;
      if (this.fleet != null)
      {
         this.fleet = null;
         oldValue.withoutShips(this);
      }
      this.fleet = value;
      if (value != null)
      {
         value.withShips(this);
      }
      this.firePropertyChange(PROPERTY_FLEET, oldValue, value);
      return this;
   }

   public Integer getHealth()
   {
      return this.health;
   }

   public Ship setHealth(Integer value)
   {
      if (Objects.equals(value, this.health))
      {
         return this;
      }

      final Integer oldValue = this.health;
      this.health = value;
      this.firePropertyChange(PROPERTY_HEALTH, oldValue, value);
      return this;
   }

   public Integer getExperience()
   {
      return this.experience;
   }

   public Ship setExperience(Integer value)
   {
      if (Objects.equals(value, this.experience))
      {
         return this;
      }

      final Integer oldValue = this.experience;
      this.experience = value;
      this.firePropertyChange(PROPERTY_EXPERIENCE, oldValue, value);
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
      result.append(' ').append(this.getType());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setEmpire(null);
      this.setFleet(null);
   }
}
