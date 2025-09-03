package de.uniks.stp24.model.game;
import java.time.LocalDateTime;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class War
{
   public static final String PROPERTY_CREATED_AT = "createdAt";
   public static final String PROPERTY_UPDATED_AT = "updatedAt";
   public static final String PROPERTY__ID = "_id";
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_DEFENDER = "defender";
   public static final String PROPERTY_ATTACKER = "attacker";
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
   private String _id;
   private String name;
   private Empire defender;
   private Empire attacker;
   protected PropertyChangeSupport listeners;

   public LocalDateTime getCreatedAt()
   {
      return this.createdAt;
   }

   public War setCreatedAt(LocalDateTime value)
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

   public War setUpdatedAt(LocalDateTime value)
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

   public War set_id(String value)
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

   public String getName()
   {
      return this.name;
   }

   public War setName(String value)
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

   public Empire getDefender()
   {
      return this.defender;
   }

   public War setDefender(Empire value)
   {
      if (this.defender == value)
      {
         return this;
      }

      final Empire oldValue = this.defender;
      if (this.defender != null)
      {
         this.defender = null;
         oldValue.withoutDefendingWars(this);
      }
      this.defender = value;
      if (value != null)
      {
         value.withDefendingWars(this);
      }
      this.firePropertyChange(PROPERTY_DEFENDER, oldValue, value);
      return this;
   }

   public Empire getAttacker()
   {
      return this.attacker;
   }

   public War setAttacker(Empire value)
   {
      if (this.attacker == value)
      {
         return this;
      }

      final Empire oldValue = this.attacker;
      if (this.attacker != null)
      {
         this.attacker = null;
         oldValue.withoutAttackingWars(this);
      }
      this.attacker = value;
      if (value != null)
      {
         value.withAttackingWars(this);
      }
      this.firePropertyChange(PROPERTY_ATTACKER, oldValue, value);
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
      this.setDefender(null);
      this.setAttacker(null);
   }
}
